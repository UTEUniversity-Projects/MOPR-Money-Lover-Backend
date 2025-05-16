package com.mobile.api.service;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.dto.category.CategoryDetailStatisticsDto;
import com.mobile.api.dto.category.CategoryDto;
import com.mobile.api.dto.category.CategoryStatisticsDto;
import com.mobile.api.dto.category.PeriodBreakdownDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.mapper.CategoryMapper;
import com.mobile.api.model.criteria.CategoryCriteria;
import com.mobile.api.model.entity.Bill;
import com.mobile.api.model.entity.Category;
import com.mobile.api.repository.jpa.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * Service for detailed category statistics and period breakdown analysis
 */
@Service
public class CategoryStatisticsService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @PersistenceContext
    private EntityManager entityManager;

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    private static final ZoneOffset UTC = ZoneOffset.UTC;

    /**
     * Generate basic statistics for a specific category within a date range
     *
     * @param categoryCriteria The criteria to find the category
     * @param startDate The start date of the analysis period (inclusive)
     * @param endDate The end date of the analysis period (inclusive)
     * @return Basic statistics for the category
     */
    public CategoryStatisticsDto getStatistics(
            CategoryCriteria categoryCriteria,
            Instant startDate,
            Instant endDate
    ) {
        // Validate parameters
        if (categoryCriteria == null || categoryCriteria.getId() == null || categoryCriteria.getUserId() == null
                || startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new BusinessException(ErrorCode.SYSTEM_INVALID_PARAMETER);
        }

        // Get category based on criteria
        Category category = categoryRepository.findOne(categoryCriteria.getSpecification())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
        CategoryDto categoryDto = categoryMapper.fromEntityToSimpleCategory(category);

        // Calculate total amount for the category in the date range
        BigDecimal totalAmount = calculateTotalAmount(
                categoryCriteria.getId(),
                categoryCriteria.getUserId(),
                startDate,
                endDate
        );

        // Create the statistics DTO
        CategoryStatisticsDto statisticsDto = new CategoryStatisticsDto();
        statisticsDto.setCategory(categoryDto);
        statisticsDto.setTotalAmount(totalAmount);

        return statisticsDto;
    }

    /**
     * Generate detailed statistics for a specific category within a date range
     *
     * @param categoryCriteria The criteria to find the category
     * @param startDate The start date of the analysis period (inclusive)
     * @param endDate The end date of the analysis period (inclusive)
     * @param periodType The period type for breakdown (from BaseConstant)
     * @return Detailed statistics and period breakdown
     */
    public CategoryDetailStatisticsDto getDetailStatistics(
            CategoryCriteria categoryCriteria,
            Instant startDate,
            Instant endDate,
            Integer periodType) {

        // Validate parameters
        if (categoryCriteria == null || categoryCriteria.getId() == null || categoryCriteria.getUserId() == null
                || periodType == null || startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new BusinessException(ErrorCode.SYSTEM_INVALID_PARAMETER);
        }

        // Get category based on criteria
        Category category = categoryRepository.findOne(categoryCriteria.getSpecification())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
        CategoryDto categoryDto = categoryMapper.fromEntityToSimpleCategory(category);

        // Calculate total amount for the category in the date range
        BigDecimal totalAmount = calculateTotalAmount(
                categoryCriteria.getId(),
                categoryCriteria.getUserId(),
                startDate,
                endDate
        );

        // Calculate days in range directly with Instant.until()
        long daysInRange = ChronoUnit.DAYS.between(startDate, endDate) + 1; // Add 1 to include end date

        // Calculate daily average
        BigDecimal dailyAverage = BigDecimal.ZERO;
        if (daysInRange > 0) {
            dailyAverage = totalAmount.divide(BigDecimal.valueOf(daysInRange), 2, RoundingMode.HALF_UP);
        }

        // Determine appropriate period type if not provided or CUSTOM
        if (periodType.equals(BaseConstant.PERIOD_TYPE_CUSTOM)) {
            periodType = determinePeriodType(daysInRange);
        }

        // Create the result DTO
        CategoryDetailStatisticsDto resultDto = new CategoryDetailStatisticsDto();
        resultDto.setCategory(categoryDto);
        resultDto.setTotalAmount(totalAmount);
        resultDto.setDailyAverage(dailyAverage);
        resultDto.setStartDate(startDate);
        resultDto.setEndDate(endDate);
        resultDto.setPeriodType(periodType);

        // Generate period breakdown
        List<PeriodBreakdownDto> breakdown = generatePeriodBreakdown(
                categoryCriteria.getId(),
                categoryCriteria.getUserId(),
                startDate,
                endDate,
                periodType,
                daysInRange
        );
        resultDto.setPeriodBreakdown(breakdown);

        return resultDto;
    }

    /**
     * Calculate total amount for a category within a date range, ensuring user ownership
     */
    private BigDecimal calculateTotalAmount(Long categoryId, Long userId, Instant startDate, Instant endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
        Root<Bill> root = query.from(Bill.class);

        // Join to Category to check userId
        Join<Bill, Category> categoryJoin = root.join("category", JoinType.INNER);

        // Predicate for category ID, user ID, and date range
        Predicate categoryPredicate = cb.equal(categoryJoin.get("id"), categoryId);
        Predicate userPredicate = cb.equal(categoryJoin.get("user").get("id"), userId);
        Predicate dateRangePredicate = cb.between(root.get("date"), startDate, endDate);

        // Combine predicates and add status check
        Predicate finalPredicate = cb.and(
                categoryPredicate,
                userPredicate,
                dateRangePredicate
        );

        // Sum the amount and convert to BigDecimal
        Expression<BigDecimal> sumExpression = cb.sum(cb.toBigDecimal(root.get("amount")));
        query.select(sumExpression).where(finalPredicate);

        BigDecimal result = entityManager.createQuery(query).getSingleResult();
        return (result != null) ? result : BigDecimal.ZERO;
    }

    /**
     * Determine the most appropriate period type based on date range length
     */
    private Integer determinePeriodType(long daysInRange) {
        if (daysInRange > 365) {
            return BaseConstant.PERIOD_TYPE_YEAR;
        } else if (daysInRange > 90) {
            return BaseConstant.PERIOD_TYPE_QUARTER;
        } else if (daysInRange > 28) {
            return BaseConstant.PERIOD_TYPE_MONTH;
        } else if (daysInRange > 7) {
            return BaseConstant.PERIOD_TYPE_WEEK;
        } else {
            // For very short periods, break down by day
            return BaseConstant.PERIOD_TYPE_CUSTOM;
        }
    }

    /**
     * Generate period breakdown based on period type
     */
    private List<PeriodBreakdownDto> generatePeriodBreakdown(
            Long categoryId, Long userId, Instant startDate, Instant endDate, Integer periodType, long daysInRange
    ) {
        if (BaseConstant.PERIOD_TYPE_WEEK.equals(periodType)) {
            // Break down by day
            return breakdownByDay(categoryId, userId, startDate, endDate);
        } else if (BaseConstant.PERIOD_TYPE_MONTH.equals(periodType)) {
            // Break down by week
            return breakdownByWeek(categoryId, userId, startDate, endDate);
        } else if (BaseConstant.PERIOD_TYPE_QUARTER.equals(periodType)) {
            // Break down by month
            return breakdownByMonth(categoryId, userId, startDate, endDate);
        } else if (BaseConstant.PERIOD_TYPE_YEAR.equals(periodType)) {
            // Break down by quarter
            return breakdownByQuarter(categoryId, userId, startDate, endDate);
        } else {
            // PERIOD_TYPE_CUSTOM or other values
            // Determine the appropriate breakdown
            if (daysInRange > 365) {
                return breakdownByYear(categoryId, userId, startDate, endDate);
            } else if (daysInRange > 90) {
                return breakdownByQuarter(categoryId, userId, startDate, endDate);
            } else if (daysInRange > 28) {
                return breakdownByMonth(categoryId, userId, startDate, endDate);
            } else if (daysInRange > 7) {
                return breakdownByWeek(categoryId, userId, startDate, endDate);
            } else {
                return  breakdownByDay(categoryId, userId, startDate, endDate);
            }
        }
    }

    /**
     * Break down by day
     */
    private List<PeriodBreakdownDto> breakdownByDay(Long categoryId, Long userId, Instant startDate, Instant endDate) {
        List<PeriodBreakdownDto> result = new ArrayList<>();

        // Start at the beginning of the start date day (truncate time part)
        Instant current = startDate.truncatedTo(ChronoUnit.DAYS);

        while (!current.isAfter(endDate)) {
            // End of day is start of next day minus 1 nanosecond
            Instant nextDay = current.plus(1, ChronoUnit.DAYS);
            Instant dayEnd = nextDay.minus(1, ChronoUnit.NANOS);

            // Ensure we don't go beyond the original end date
            if (dayEnd.isAfter(endDate)) {
                dayEnd = endDate;
            }

            BigDecimal amount = calculateTotalAmount(categoryId, userId, current, dayEnd);

            // Create a breakdown entry
            PeriodBreakdownDto breakdown = new PeriodBreakdownDto();
            breakdown.setStartDate(current);
            breakdown.setEndDate(dayEnd);

            // Format date for label - need to use ZonedDateTime for formatting
            String label = DateTimeFormatter.ofPattern("MMM dd")
                    .format(current.atZone(UTC));

            breakdown.setLabel(label);
            breakdown.setAmount(amount);

            result.add(breakdown);

            // Move to next day
            current = nextDay;
        }

        return result;
    }

    /**
     * Break down by week
     */
    private List<PeriodBreakdownDto> breakdownByWeek(Long categoryId, Long userId, Instant startDate, Instant endDate) {
        List<PeriodBreakdownDto> result = new ArrayList<>();

        // Get the start of week (Monday) that contains the start date
        // First convert to ZonedDateTime to use DayOfWeek methods
        ZonedDateTime startZdt = startDate.atZone(UTC);
        int daysToSubtract = (startZdt.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue() + 7) % 7;

        // Get first day of week (Monday)
        Instant weekStart = startZdt.minusDays(daysToSubtract).truncatedTo(ChronoUnit.DAYS).toInstant();

        while (!weekStart.isAfter(endDate)) {
            // End of week is start of next week minus 1 nanosecond
            Instant weekEnd = weekStart.plus(7, ChronoUnit.DAYS).minus(1, ChronoUnit.NANOS);

            // Ensure we don't go outside the original date range
            Instant periodStart = weekStart;
            if (periodStart.isBefore(startDate)) {
                periodStart = startDate;
            }

            Instant periodEnd = weekEnd;
            if (periodEnd.isAfter(endDate)) {
                periodEnd = endDate;
            }

            BigDecimal amount = calculateTotalAmount(categoryId, userId, periodStart, periodEnd);

            // Create a breakdown entry
            PeriodBreakdownDto breakdown = new PeriodBreakdownDto();
            breakdown.setStartDate(periodStart);
            breakdown.setEndDate(periodEnd);

            // Format dates for label
            ZonedDateTime periodStartZdt = periodStart.atZone(UTC);
            ZonedDateTime periodEndZdt = periodEnd.atZone(UTC);
            int weekOfYear = periodStartZdt.get(WeekFields.ISO.weekOfYear());

            String label = String.format("Week %d (%s - %s)",
                    weekOfYear,
                    periodStartZdt.format(DateTimeFormatter.ofPattern("MMM dd")),
                    periodEndZdt.format(DateTimeFormatter.ofPattern("MMM dd")));

            breakdown.setLabel(label);
            breakdown.setAmount(amount);

            result.add(breakdown);

            // Move to next week
            weekStart = weekStart.plus(7, ChronoUnit.DAYS);
        }

        return result;
    }

    /**
     * Break down by month
     */
    private List<PeriodBreakdownDto> breakdownByMonth(Long categoryId, Long userId, Instant startDate, Instant endDate) {
        List<PeriodBreakdownDto> result = new ArrayList<>();

        // Get the start of month that contains the start date
        ZonedDateTime startZdt = startDate.atZone(UTC);
        ZonedDateTime monthStartZdt = startZdt.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        Instant monthStart = monthStartZdt.toInstant();

        while (!monthStart.isAfter(endDate)) {
            // Get end of month
            YearMonth yearMonth = YearMonth.from(monthStartZdt);
            ZonedDateTime monthEndZdt = monthStartZdt
                    .withDayOfMonth(yearMonth.lengthOfMonth())
                    .plusDays(1)
                    .truncatedTo(ChronoUnit.DAYS)
                    .minusNanos(1);

            Instant monthEnd = monthEndZdt.toInstant();

            // Ensure we don't go outside the original date range
            Instant periodStart = monthStart;
            if (periodStart.isBefore(startDate)) {
                periodStart = startDate;
            }

            Instant periodEnd = monthEnd;
            if (periodEnd.isAfter(endDate)) {
                periodEnd = endDate;
            }

            BigDecimal amount = calculateTotalAmount(categoryId, userId, periodStart, periodEnd);

            // Create a breakdown entry
            PeriodBreakdownDto breakdown = new PeriodBreakdownDto();
            breakdown.setStartDate(periodStart);
            breakdown.setEndDate(periodEnd);

            // Format month and year for label
            String monthName = monthStartZdt.getMonth().getDisplayName(TextStyle.FULL, DEFAULT_LOCALE);
            String label = String.format("%s %d", monthName, monthStartZdt.getYear());

            breakdown.setLabel(label);
            breakdown.setAmount(amount);

            result.add(breakdown);

            // Move to next month
            monthStartZdt = monthStartZdt.plusMonths(1);
            monthStart = monthStartZdt.toInstant();
        }

        return result;
    }

    /**
     * Break down by quarter
     */
    private List<PeriodBreakdownDto> breakdownByQuarter(Long categoryId, Long userId, Instant startDate, Instant endDate) {
        List<PeriodBreakdownDto> result = new ArrayList<>();

        // Get the start of quarter that contains the start date
        ZonedDateTime startZdt = startDate.atZone(UTC);
        int month = startZdt.getMonthValue();
        int quarterStartMonth = ((month - 1) / 3) * 3 + 1;

        ZonedDateTime quarterStartZdt = startZdt
                .withMonth(quarterStartMonth)
                .withDayOfMonth(1)
                .truncatedTo(ChronoUnit.DAYS);

        Instant quarterStart = quarterStartZdt.toInstant();

        while (!quarterStart.isAfter(endDate)) {
            // Get end of quarter (last day of third month)
            ZonedDateTime quarterEndZdt = quarterStartZdt
                    .plusMonths(3)
                    .withDayOfMonth(1)
                    .truncatedTo(ChronoUnit.DAYS)
                    .minusNanos(1);

            Instant quarterEnd = quarterEndZdt.toInstant();

            // Ensure we don't go outside the original date range
            Instant periodStart = quarterStart;
            if (periodStart.isBefore(startDate)) {
                periodStart = startDate;
            }

            Instant periodEnd = quarterEnd;
            if (periodEnd.isAfter(endDate)) {
                periodEnd = endDate;
            }

            BigDecimal amount = calculateTotalAmount(categoryId, userId, periodStart, periodEnd);

            // Create a breakdown entry
            PeriodBreakdownDto breakdown = new PeriodBreakdownDto();
            breakdown.setStartDate(periodStart);
            breakdown.setEndDate(periodEnd);

            // Calculate quarter number (1-4)
            int quarter = (quarterStartZdt.getMonthValue() - 1) / 3 + 1;

            // Format quarter and year for label
            String label = String.format("Q%d %d", quarter, quarterStartZdt.getYear());

            breakdown.setLabel(label);
            breakdown.setAmount(amount);

            result.add(breakdown);

            // Move to next quarter
            quarterStartZdt = quarterStartZdt.plusMonths(3);
            quarterStart = quarterStartZdt.toInstant();
        }

        return result;
    }

    /**
     * Break down by year
     */
    private List<PeriodBreakdownDto> breakdownByYear(Long categoryId, Long userId, Instant startDate, Instant endDate) {
        List<PeriodBreakdownDto> result = new ArrayList<>();

        // Get the start of year that contains the start date
        ZonedDateTime startZdt = startDate.atZone(UTC);
        ZonedDateTime yearStartZdt = startZdt
                .withMonth(1)
                .withDayOfMonth(1)
                .truncatedTo(ChronoUnit.DAYS);

        Instant yearStart = yearStartZdt.toInstant();

        while (!yearStart.isAfter(endDate)) {
            // Get end of year (December 31, 23:59:59.999999999)
            ZonedDateTime yearEndZdt = yearStartZdt
                    .plusYears(1)
                    .truncatedTo(ChronoUnit.DAYS)
                    .minusNanos(1);

            Instant yearEnd = yearEndZdt.toInstant();

            // Ensure we don't go outside the original date range
            Instant periodStart = yearStart;
            if (periodStart.isBefore(startDate)) {
                periodStart = startDate;
            }

            Instant periodEnd = yearEnd;
            if (periodEnd.isAfter(endDate)) {
                periodEnd = endDate;
            }

            BigDecimal amount = calculateTotalAmount(categoryId, userId, periodStart, periodEnd);

            // Create a breakdown entry
            PeriodBreakdownDto breakdown = new PeriodBreakdownDto();
            breakdown.setStartDate(periodStart);
            breakdown.setEndDate(periodEnd);

            // Format year for label
            String label = String.valueOf(yearStartZdt.getYear());

            breakdown.setLabel(label);
            breakdown.setAmount(amount);

            result.add(breakdown);

            // Move to next year
            yearStartZdt = yearStartZdt.plusYears(1);
            yearStart = yearStartZdt.toInstant();
        }

        return result;
    }
}
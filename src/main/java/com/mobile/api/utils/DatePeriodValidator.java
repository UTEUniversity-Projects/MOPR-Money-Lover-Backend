package com.mobile.api.utils;

import com.mobile.api.constant.BaseConstant;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for validating date ranges based on budget period types.
 * Works directly with UTC time (Instant objects).
 */
public final class DatePeriodValidator {

    public DatePeriodValidator() {
        // Prevent instantiation
    }

    /**
     * Validates if the start and end dates match the requirements for the given period type.
     *
     * @param periodType the selected period type from BaseConstant
     * @param startDate the start date as Instant (in UTC)
     * @param endDate the end date as Instant (in UTC)
     * @return true if the date range is valid for the given period type, false otherwise
     */
    public static boolean isValidPeriod(Integer periodType, Instant startDate, Instant endDate) {
        if (startDate == null || endDate == null || periodType == null) {
            return false;
        }

        // Make sure end date is after start date
        if (endDate.isBefore(startDate) || endDate.equals(startDate)) {
            return false;
        }

        // Get start and end date components in UTC
        LocalDate startLocalDate = startDate.atZone(ZoneOffset.UTC).toLocalDate();
        LocalDate endLocalDate = endDate.atZone(ZoneOffset.UTC).toLocalDate();

        // Get time components to check for midnight and end of day
        LocalTime startTime = startDate.atZone(ZoneOffset.UTC).toLocalTime();
        LocalTime endTime = endDate.atZone(ZoneOffset.UTC).toLocalTime();

        // Check if startDate is at midnight (00:00:00) and endDate is at 23:59:59
        boolean isStartAtMidnight = startTime.equals(LocalTime.MIDNIGHT);
        boolean isEndAtDayEnd = endTime.getHour() == 23 && endTime.getMinute() == 59 && endTime.getSecond() == 59;

        if (!isStartAtMidnight || !isEndAtDayEnd) {
            return false;
        }

        // Check specific period type requirements
        if (periodType.equals(BaseConstant.PERIOD_TYPE_WEEK)) {
            // For a week: end date should be exactly 6 days after start date
            return ChronoUnit.DAYS.between(startLocalDate, endLocalDate) == 6;

        } else if (periodType.equals(BaseConstant.PERIOD_TYPE_MONTH)) {
            // For a month: start should be 1st of month, end should be last day of same month
            int startDay = startLocalDate.getDayOfMonth();
            if (startDay != 1) {
                return false;
            }

            // End date should be last day of same month
            return endLocalDate.equals(startLocalDate.withDayOfMonth(
                    startLocalDate.getMonth().length(startLocalDate.isLeapYear())));

        } else if (periodType.equals(BaseConstant.PERIOD_TYPE_QUARTER)) {
            // For quarter: start should be 1st day of quarter month (1,4,7,10)
            int startDay = startLocalDate.getDayOfMonth();
            int startMonth = startLocalDate.getMonthValue();

            if (startDay != 1 || (startMonth != 1 && startMonth != 4 && startMonth != 7 && startMonth != 10)) {
                return false;
            }

            // End date should be last day of the quarter
            int endMonth = startMonth + 2; // last month in quarter
            YearMonth endYearMonth = YearMonth.of(startLocalDate.getYear(), endMonth);
            LocalDate expectedEndDate = LocalDate.of(
                    startLocalDate.getYear(),
                    endMonth,
                    endYearMonth.lengthOfMonth());

            return endLocalDate.equals(expectedEndDate);

        } else if (periodType.equals(BaseConstant.PERIOD_TYPE_YEAR)) {
            // For year: start should be Jan 1, end should be Dec 31 of same year
            if (startLocalDate.getDayOfMonth() != 1 || startLocalDate.getMonthValue() != 1) {
                return false;
            }

            // End date should be December 31 of start year
            LocalDate expectedEndDate = LocalDate.of(startLocalDate.getYear(), 12, 31);
            return endLocalDate.equals(expectedEndDate);

        } else {
            // For custom type, no specific validation
            return periodType.equals(BaseConstant.PERIOD_TYPE_CUSTOM);
        }
    }
}
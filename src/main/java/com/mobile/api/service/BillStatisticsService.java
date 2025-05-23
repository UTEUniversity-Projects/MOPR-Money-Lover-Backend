package com.mobile.api.service;

import com.mobile.api.dto.bill.BillDetailStatisticsDto;
import com.mobile.api.dto.bill.BillDto;
import com.mobile.api.dto.bill.BillStatisticsDto;
import com.mobile.api.dto.category.CategoryStatisticsDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.category.CategoryDto;
import com.mobile.api.mapper.BillMapper;
import com.mobile.api.mapper.CategoryMapper;
import com.mobile.api.model.criteria.BillCriteria;
import com.mobile.api.model.entity.Bill;
import com.mobile.api.model.entity.Category;
import com.mobile.api.repository.jpa.BillRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Service class for generating statistics related to bills
 */
@Service
public class BillStatisticsService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Generate statistics based on bill criteria and pagination
     *
     * @param billCriteria The search criteria for bills
     * @param pageable Pagination information
     * @return BillStatisticsDto containing pagination and statistics data
     */
    public BillStatisticsDto getStatistics(BillCriteria billCriteria, Pageable pageable) {
        Specification<Bill> specification = billCriteria.getSpecification();
        Page<Bill> page = billRepository.findAll(specification, pageable);

        // Create PaginationDto with the list of BillDto
        PaginationDto<BillDto> paginationResult = new PaginationDto<>(
                billMapper.fromEntitiesToBillDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        // Create BillStatisticsDto
        BillStatisticsDto billStatisticsDto = new BillStatisticsDto();
        billStatisticsDto.setPagination(paginationResult);

        // First, get overall income and expense totals
        BigDecimal[] overallTotals = calculateOverallTotals(specification);
        billStatisticsDto.setTotalIncome(overallTotals[0]);
        billStatisticsDto.setTotalExpense(overallTotals[1]);

        return billStatisticsDto;
    }

    /**
     * Generate detailed statistics based on bill criteria
     *
     * @param billCriteria The search criteria for bills
     * @return BillDetailStatisticsDto containing statistics data
     */
    public BillDetailStatisticsDto getDetailStatistics(BillCriteria billCriteria) {
        // Create BillDetailStatisticsDto
        BillDetailStatisticsDto billStatisticsDto = new BillDetailStatisticsDto();

        // Calculate income, expense totals and category statistics
        enrichStatistics(billCriteria.getSpecification(), billStatisticsDto);

        return billStatisticsDto;
    }

    /**
     * Enrich the statistics DTO with income/expense totals and category statistics
     *
     * @param specification The specification to filter bills
     * @param billStatisticsDto The DTO to be enriched
     */
    private void enrichStatistics(Specification<Bill> specification, BillDetailStatisticsDto billStatisticsDto) {
        // First, get overall income and expense totals
        BigDecimal[] overallTotals = calculateOverallTotals(specification);
        billStatisticsDto.setTotalIncome(overallTotals[0]);
        billStatisticsDto.setTotalExpense(overallTotals[1]);

        // Then, calculate statistics by category
        List<Object[]> categoryStats = calculateCategoryStatistics(specification);

        for (Object[] stat : categoryStats) {
            CategoryStatisticsDto categoryStatDto = new CategoryStatisticsDto();

            // Get category entity and convert to DTO
            Category category = (Category) stat[0];
            CategoryDto categoryDto = categoryMapper.fromEntityToSimpleCategory(category);
            categoryStatDto.setCategory(categoryDto);

            // Get total amount for this category
            // The second element is now BigDecimal, not Double, so handle accordingly
            BigDecimal totalAmount;
            if (stat[1] == null) {
                totalAmount = BigDecimal.ZERO;
            } else if (stat[1] instanceof BigDecimal) {
                totalAmount = (BigDecimal) stat[1];
            } else if (stat[1] instanceof Number) {
                totalAmount = BigDecimal.valueOf(((Number) stat[1]).doubleValue());
            } else {
                // Fallback for unexpected types
                totalAmount = BigDecimal.ZERO;
            }

            categoryStatDto.setTotalAmount(totalAmount);

            // Calculate percentage based on category type (income or expense)
            BigDecimal percentage = BigDecimal.ZERO;
            if (category.getIsExpense() && billStatisticsDto.getTotalExpense().compareTo(BigDecimal.ZERO) > 0) {
                // Calculate percentage of total expense
                percentage = totalAmount
                        .multiply(BigDecimal.valueOf(100))
                        .divide(billStatisticsDto.getTotalExpense(), 2, RoundingMode.HALF_UP);

                // Add to expense categories list
                categoryStatDto.setPercentage(percentage);
                billStatisticsDto.getExpenseByCategories().add(categoryStatDto);
            } else if (!category.getIsExpense() && billStatisticsDto.getTotalIncome().compareTo(BigDecimal.ZERO) > 0) {
                // Calculate percentage of total income
                percentage = totalAmount
                        .multiply(BigDecimal.valueOf(100))
                        .divide(billStatisticsDto.getTotalIncome(), 2, RoundingMode.HALF_UP);

                // Add to income categories list
                categoryStatDto.setPercentage(percentage);
                billStatisticsDto.getIncomeByCategories().add(categoryStatDto);
            } else {
                // Handle case when there's no income or expense
                categoryStatDto.setPercentage(BigDecimal.ZERO);

                // Add to appropriate list based on category type
                if (category.getIsExpense()) {
                    billStatisticsDto.getExpenseByCategories().add(categoryStatDto);
                } else {
                    billStatisticsDto.getIncomeByCategories().add(categoryStatDto);
                }
            }
        }

        // Sort categories by total amount (descending)
        Comparator<CategoryStatisticsDto> byAmountDesc =
                (cat1, cat2) -> cat2.getTotalAmount().compareTo(cat1.getTotalAmount());

        billStatisticsDto.getIncomeByCategories().sort(byAmountDesc);
        billStatisticsDto.getExpenseByCategories().sort(byAmountDesc);
    }

    /**
     * Calculate statistics for each category
     *
     * @param specification The specification to filter bills
     * @return List of Object[] arrays containing [Category, totalAmount as BigDecimal]
     */
    private List<Object[]> calculateCategoryStatistics(Specification<Bill> specification) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Bill> root = query.from(Bill.class);

        // Join with Category
        Join<Bill, Category> categoryJoin = root.join("category", JoinType.INNER);

        // Apply the specification filter
        Predicate specPredicate = specification.toPredicate(root, query, cb);

        // Group by category and calculate sum, ensuring BigDecimal type
        Expression<BigDecimal> sumExpression = cb.sum(cb.toBigDecimal(root.get("amount")));

        query.multiselect(
                        categoryJoin,
                        sumExpression
                )
                .where(specPredicate)
                .groupBy(categoryJoin);

        // Execute query and return results
        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Calculate overall income and expense totals
     *
     * @param specification The specification to filter bills
     * @return Array containing [totalIncome, totalExpense]
     */
    private BigDecimal[] calculateOverallTotals(Specification<Bill> specification) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Bill> root = query.from(Bill.class);

        // Join with Category to access isExpense flag
        Join<Object, Object> categoryJoin = root.join("category", JoinType.INNER);

        // Apply the specification filter
        Predicate specPredicate = specification.toPredicate(root, query, cb);

        // Calculate total income (when isExpense = false)
        Expression<BigDecimal> incomeExpr = cb.sum(
                cb.<BigDecimal>selectCase()
                        .when(cb.equal(categoryJoin.get("isExpense"), false), cb.toBigDecimal(root.get("amount")))
                        .otherwise(BigDecimal.ZERO)
        );

        // Calculate total expense (when isExpense = true)
        Expression<BigDecimal> expenseExpr = cb.sum(
                cb.<BigDecimal>selectCase()
                        .when(cb.equal(categoryJoin.get("isExpense"), true), cb.toBigDecimal(root.get("amount")))
                        .otherwise(BigDecimal.ZERO)
        );

        // Execute query to get both income and expense in one go
        query.multiselect(
                incomeExpr.alias("income"),
                expenseExpr.alias("expense")
        ).where(specPredicate);

        Tuple result;
        try {
            result = entityManager.createQuery(query).getSingleResult();
        } catch (Exception e) {
            // In case of any exception, return zeros
            return new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO };
        }

        // Extract results and handle potential nulls and type conversions
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        try {
            Object incomeObj = result.get("income");
            if (incomeObj != null) {
                if (incomeObj instanceof BigDecimal) {
                    totalIncome = (BigDecimal) incomeObj;
                } else if (incomeObj instanceof Number) {
                    totalIncome = BigDecimal.valueOf(((Number) incomeObj).doubleValue());
                }
            }
        } catch (Exception e) {
            // Keep default value of ZERO if there's an issue
        }

        try {
            Object expenseObj = result.get("expense");
            if (expenseObj != null) {
                if (expenseObj instanceof BigDecimal) {
                    totalExpense = (BigDecimal) expenseObj;
                } else if (expenseObj instanceof Number) {
                    totalExpense = BigDecimal.valueOf(((Number) expenseObj).doubleValue());
                }
            }
        } catch (Exception e) {
            // Keep default value of ZERO if there's an issue
        }

        return new BigDecimal[] { totalIncome, totalExpense };
    }
}
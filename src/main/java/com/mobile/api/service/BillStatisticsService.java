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
     * @return BillDetailStatisticsDto containing pagination and statistics data
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

        // Create BillDetailStatisticsDto
        BillStatisticsDto billStatisticsDto = new BillStatisticsDto();
        billStatisticsDto.setPagination(paginationResult);

        // First, get overall income and expense totals
        BigDecimal[] overallTotals = calculateOverallTotals(specification);
        billStatisticsDto.setTotalIncome(overallTotals[0]);
        billStatisticsDto.setTotalExpense(overallTotals[1]);

        return billStatisticsDto;
    }

    /**
     * Generate statistics based on bill criteria and pagination
     *
     * @param billCriteria The search criteria for bills
     * @return BillDetailStatisticsDto containing pagination and statistics data
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
            Double totalAmount = (Double) stat[1];
            if (totalAmount == null) totalAmount = 0.0;
            categoryStatDto.setTotalAmount(BigDecimal.valueOf(totalAmount));

            // Calculate percentage based on category type (income or expense)
            BigDecimal percentage = BigDecimal.ZERO;
            if (category.getIsExpense() && billStatisticsDto.getTotalExpense().compareTo(BigDecimal.ZERO) > 0) {
                // Calculate percentage of total expense
                percentage = BigDecimal.valueOf(totalAmount)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(billStatisticsDto.getTotalExpense(), 2, RoundingMode.HALF_UP);

                // Add to expense categories list
                categoryStatDto.setPercentage(percentage);
                billStatisticsDto.getExpenseByCategories().add(categoryStatDto);
            } else if (!category.getIsExpense() && billStatisticsDto.getTotalIncome().compareTo(BigDecimal.ZERO) > 0) {
                // Calculate percentage of total income
                percentage = BigDecimal.valueOf(totalAmount)
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
        Expression<Double> incomeExpr = cb.sum(
                cb.<Double>selectCase()
                        .when(cb.equal(categoryJoin.get("isExpense"), false), root.get("amount"))
                        .otherwise(0.0)
        );

        // Calculate total expense (when isExpense = true)
        Expression<Double> expenseExpr = cb.sum(
                cb.<Double>selectCase()
                        .when(cb.equal(categoryJoin.get("isExpense"), true), root.get("amount"))
                        .otherwise(0.0)
        );

        // Execute query to get both income and expense in one go
        query.multiselect(
                incomeExpr.alias("income"),
                expenseExpr.alias("expense")
        ).where(specPredicate);

        Tuple result = entityManager.createQuery(query).getSingleResult();

        // Extract results and convert to BigDecimal
        Double income = result.get("income", Double.class);
        Double expense = result.get("expense", Double.class);

        // Handle null values
        BigDecimal totalIncome = (income != null) ? BigDecimal.valueOf(income) : BigDecimal.ZERO;
        BigDecimal totalExpense = (expense != null) ? BigDecimal.valueOf(expense) : BigDecimal.ZERO;

        return new BigDecimal[] { totalIncome, totalExpense };
    }

    /**
     * Calculate statistics for each category
     *
     * @param specification The specification to filter bills
     * @return List of Object[] arrays containing [Category, totalAmount]
     */
    private List<Object[]> calculateCategoryStatistics(Specification<Bill> specification) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Bill> root = query.from(Bill.class);

        // Join with Category
        Join<Bill, Category> categoryJoin = root.join("category", JoinType.INNER);

        // Apply the specification filter
        Predicate specPredicate = specification.toPredicate(root, query, cb);

        // Group by category and calculate sum
        query.multiselect(
                        categoryJoin,
                        cb.sum(root.get("amount"))
                )
                .where(specPredicate)
                .groupBy(categoryJoin);

        // Execute query and return results
        return entityManager.createQuery(query).getResultList();
    }
}
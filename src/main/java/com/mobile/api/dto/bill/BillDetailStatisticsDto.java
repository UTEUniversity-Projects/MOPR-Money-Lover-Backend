package com.mobile.api.dto.bill;

import com.mobile.api.dto.category.CategoryStatisticsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class BillDetailStatisticsDto {
    @Schema(description = "totalIncome")
    private BigDecimal totalIncome;

    @Schema(description = "totalExpense")
    private BigDecimal totalExpense;

    @Schema(description = "Statistics grouped by income categories")
    private List<CategoryStatisticsDto> incomeByCategories = new ArrayList<>();

    @Schema(description = "Statistics grouped by expense categories")
    private List<CategoryStatisticsDto> expenseByCategories = new ArrayList<>();
}
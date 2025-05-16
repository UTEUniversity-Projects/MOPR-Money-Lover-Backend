package com.mobile.api.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CategoryStatisticsDto {
    @Schema(description = "Category information")
    private CategoryDto category;

    @Schema(description = "Total amount of bills in this category")
    private BigDecimal totalAmount;

    @Schema(description = "Percentage of total income/expense (based on category type)")
    private BigDecimal percentage;
}
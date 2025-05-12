package com.mobile.api.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDetailStatisticsDto {
    @Schema(description = "Category information")
    private CategoryDto category;

    @Schema(description = "Total amount for the category in the date range")
    private BigDecimal totalAmount;

    @Schema(description = "Daily average amount")
    private BigDecimal dailyAverage;

    @Schema(description = "Start date of the analysis period")
    private Instant startDate;

    @Schema(description = "End date of the analysis period")
    private Instant endDate;

    @Schema(description = "Period type used for breakdown")
    private Integer periodType;

    @Schema(description = "Period breakdown data")
    private List<PeriodBreakdownDto> periodBreakdown = new ArrayList<>();
}
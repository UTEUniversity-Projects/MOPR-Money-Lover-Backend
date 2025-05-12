package com.mobile.api.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PeriodBreakdownDto {
    @Schema(description = "Start date of the period")
    private Instant startDate;

    @Schema(description = "End date of the period")
    private Instant endDate;

    @Schema(description = "Label for the period (e.g., 'Week 1', 'January', 'Q1 2025')")
    private String label;

    @Schema(description = "Total amount for this period")
    private BigDecimal amount;
}
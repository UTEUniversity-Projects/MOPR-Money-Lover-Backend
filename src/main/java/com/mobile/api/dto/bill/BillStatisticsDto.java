package com.mobile.api.dto.bill;

import com.mobile.api.dto.PaginationDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class BillStatisticsDto {
    @Schema(description = "Pagination")
    private PaginationDto<BillDto> pagination;

    @Schema(description = "totalIncome")
    private BigDecimal totalIncome;

    @Schema(description = "totalExpense")
    private BigDecimal totalExpense;
}
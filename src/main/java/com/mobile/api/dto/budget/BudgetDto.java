package com.mobile.api.dto.budget;

import com.mobile.api.dto.category.CategoryStatisticsDto;
import com.mobile.api.dto.wallet.WalletDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "categoryStatistics")
    private CategoryStatisticsDto categoryStatistics;

    @Schema(description = "wallet")
    private WalletDto wallet;

    @Schema(description = "periodType")
    private Integer periodType;

    @Schema(description = "startDate")
    private String startDate;

    @Schema(description = "endDate")
    private String endDate;

    @Schema(description = "amount")
    private BigDecimal amount;

    @Schema(description = "spentAmount")
    private BigDecimal spentAmount;
}

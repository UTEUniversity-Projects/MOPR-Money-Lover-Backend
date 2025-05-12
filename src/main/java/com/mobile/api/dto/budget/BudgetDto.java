package com.mobile.api.dto.budget;

import com.mobile.api.dto.category.CategoryDto;
import com.mobile.api.dto.wallet.WalletDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BudgetDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "category")
    private CategoryDto category;

    @Schema(description = "wallet")
    private WalletDto wallet;

    @Schema(description = "periodType")
    private Integer periodType;

    @Schema(description = "startDate")
    private String startDate;

    @Schema(description = "endDate")
    private String endDate;

    @Schema(description = "amount")
    private Double amount;

    @Schema(description = "balance")
    private Double balance;
}

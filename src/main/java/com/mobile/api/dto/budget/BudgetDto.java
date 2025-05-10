package com.mobile.api.dto.budget;

import com.mobile.api.dto.category.CategoryDto;
import com.mobile.api.dto.period.PeriodDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BudgetDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "period")
    private PeriodDto period;

    @Schema(description = "category")
    private CategoryDto category;

    @Schema(description = "amount")
    private Double amount;

    @Schema(description = "balance")
    private Double balance;
}

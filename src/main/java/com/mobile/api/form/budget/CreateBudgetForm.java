package com.mobile.api.form.budget;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.PeriodType;
import com.mobile.api.validation.TypeDouble;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
@Schema(description = "Create Budget Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBudgetForm {
    @Schema(description = "Category ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Category ID can not be null")
    private Long categoryId;

    @Schema(description = "Wallet ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Wallet ID can not be null")
    private Long walletId;

    @Schema(description = "Period Type", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Period Type can not be null")
    @PeriodType
    private Integer periodType;

    @Schema(description = "Start Date", example = "2023-10-01T00:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Start Date can not be null")
    private Instant startDate;

    @Schema(description = "End Date", example = "2023-10-31T23:59:59Z", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "End Date can not be null")
    private Instant endDate;

    @Schema(description = "Amount", example = "1000.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeDouble(fieldName = "Amount")
    private Double amount;
}

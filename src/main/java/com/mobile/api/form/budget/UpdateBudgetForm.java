package com.mobile.api.form.budget;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeDouble;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Update Budget Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateBudgetForm {
    @Schema(description = "ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID can not be null")
    private Long id;

    @Schema(description = "Period ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Period ID can not be null")
    private Long periodId;

    @Schema(description = "Category ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Category ID can not be null")
    private Long categoryId;

    @Schema(description = "Amount", example = "1000.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeDouble(fieldName = "Amount")
    private Double amount;
}

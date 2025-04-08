package com.mobile.api.form.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Update Category Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCategoryForm {
    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID is required")
    private Long id;

    @Schema(description = "Name", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Name")
    private String name;

    @Schema(description = "Description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    @Schema(description = "Is expense", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Is expense")
    private Boolean isExpense;

    @Schema(description = "Icon ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Icon ID can not be null")
    private Long iconId;
}

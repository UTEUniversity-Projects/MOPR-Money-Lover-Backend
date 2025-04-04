package com.mobile.api.form.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Create Category Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCategoryForm {
    @Schema(description = "Name", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Name")
    private String name;

    @Schema(description = "Description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    @Schema(description = "Is expense", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Is expense")
    private Boolean isExpense;

    @Schema(description = "Icon ID", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long iconId;
}

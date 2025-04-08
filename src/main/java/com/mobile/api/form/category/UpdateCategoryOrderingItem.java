package com.mobile.api.form.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Update Category Ordering Item")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCategoryOrderingItem {
    @Schema(description = "Category ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Category ID can not be null")
    private Long categoryId;

    @Schema(description = "Ordering", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Ordering can not be null")
    private Integer ordering;
}

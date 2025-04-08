package com.mobile.api.form.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Update Category Ordering Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCategoryOrderingForm {
    @Schema(description = "Categories", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Categories can not be null")
    @Valid
    private List<UpdateCategoryOrderingItem> categories;
}

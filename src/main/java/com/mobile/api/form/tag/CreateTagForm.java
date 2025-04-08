package com.mobile.api.form.tag;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Create Tag Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTagForm {
    @Schema(description = "Name", example = "Food", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Name")
    private String name;
}

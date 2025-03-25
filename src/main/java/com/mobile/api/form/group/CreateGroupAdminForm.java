package com.mobile.api.form.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.GroupKind;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Create Group Admin Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateGroupAdminForm {
    @Schema(description = "Name", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Name")
    private String name;
    
    @Schema(description = "Description", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Description")
    private String description;

    @Schema(description = "Kind", requiredMode = Schema.RequiredMode.REQUIRED)
    @GroupKind(allowNull = false)
    private Integer kind;

    @Schema(description = "Is system group", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Is system group can not be null")
    private Boolean isSystemGroup;
}

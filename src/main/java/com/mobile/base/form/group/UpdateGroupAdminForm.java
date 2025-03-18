package com.mobile.base.form.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.base.validation.GroupKind;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Update Group Admin Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateGroupAdminForm {
    @Schema(description = "ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID cannot be null")
    private Long id;

    @Schema(description = "Name", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Name cant not be empty")
    private String name;

    @Schema(description = "Description", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Description cant not be empty")
    private String description;

    @Schema(description = "Kind", requiredMode = Schema.RequiredMode.REQUIRED)
    @GroupKind(allowNull = false)
    private Integer kind;

    @Schema(description = "Is system group", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Is system group can not be null")
    private Boolean isSystemGroup;
}

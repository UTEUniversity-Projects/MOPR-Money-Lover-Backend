package com.mobile.api.form.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Update Permission List Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePermissionListForm {
    @Schema(description = "ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID cannot be null")
    private Long id;

    @Schema(description = "Permissions", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Permissions cant not be empty")
    private Long[] permissions;
}

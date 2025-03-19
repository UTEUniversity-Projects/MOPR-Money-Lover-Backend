package com.mobile.api.form.permission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Update Permission Admin Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePermissionAdminForm {
    @Schema(description = "ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID cannot be null")
    private Long id;

    @Schema(description = "Name", example = "Get account", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Name can not be empty")
    private String name;

    @Schema(description = "Action", example = "/api/v1/account/get", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Action can not be empty")
    private String action;

    @Schema(description = "Description", example = "Get account", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Description can not be empty")
    private String description;

    @Schema(description = "Name group", example = "Account", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Name group can not be empty")
    private String nameGroup;

    @Schema(description = "Code", example = "ACC_G", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Code can not be empty")
    private String code;

    @Schema(description = "Show menu", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Show menu can not be null")
    private Boolean showMenu;
}

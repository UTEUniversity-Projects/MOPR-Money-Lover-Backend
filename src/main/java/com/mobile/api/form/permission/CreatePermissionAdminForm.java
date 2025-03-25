package com.mobile.api.form.permission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Create Permission Admin Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePermissionAdminForm {
    @Schema(description = "Name", example = "Get account", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Name")
    private String name;
    
    @Schema(description = "Action", example = "/api/v1/account/get", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Action")
    private String action;

    @Schema(description = "Description", example = "Get account", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Description")
    private String description;
    
    @Schema(description = "Name group", example = "Account", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Name group")
    private String nameGroup;

    @Schema(description = "Code", example = "ACC_G", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Code")
    private String code;

    @Schema(description = "Show menu", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Show menu can not be null")
    private Boolean showMenu;
}

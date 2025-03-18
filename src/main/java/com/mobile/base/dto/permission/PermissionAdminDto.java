package com.mobile.base.dto.permission;

import com.mobile.base.dto.BaseAdminDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionAdminDto extends BaseAdminDto {
    @Schema(name = "name")
    private String name;

    @Schema(name = "action")
    private String action;

    @Schema(name = "showMenu")
    private Boolean showMenu;

    @Schema(name = "description")
    private String description;

    @Schema(name = "nameGroup")
    private String nameGroup;

    @Schema(name = "code")
    private String code;
}

package com.mobile.api.dto.group;

import com.mobile.api.dto.BaseAdminDto;
import com.mobile.api.dto.permission.PermissionAdminDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GroupAdminDto extends BaseAdminDto {
    @Schema(description = "name")
    private String name;

    @Schema(description = "description")
    private String description;
    
    @Schema(description = "kind")
    private Integer kind;

    @Schema(description = "isSystemGroup")
    private Boolean isSystemGroup;
    
    @Schema(description = "permissions")
    private List<PermissionAdminDto> permissions;
}

package com.mobile.api.dto.account;

import com.mobile.api.dto.BaseAdminDto;
import com.mobile.api.dto.group.GroupAdminDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountAdminDto extends BaseAdminDto {
    @Schema(description = "username")
    private String username;

    @Schema(description = "email")
    private String email;

    @Schema(description = "phone number")
    private String phone;

    @Schema(description = "avatar path")
    private String avatarPath;

    @Schema(description = "group")
    private GroupAdminDto group;
}

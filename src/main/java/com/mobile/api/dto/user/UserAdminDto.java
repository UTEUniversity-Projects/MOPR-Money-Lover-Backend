package com.mobile.api.dto.user;

import com.mobile.api.dto.BaseAdminDto;
import com.mobile.api.dto.account.AccountAdminDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAdminDto extends BaseAdminDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "account")
    private AccountAdminDto account;

    @Schema(description = "fullName")
    private String fullName;

    @Schema(description = "gender")
    private Integer gender;

    @Schema(description = "birthday")
    private Instant birthday;
}

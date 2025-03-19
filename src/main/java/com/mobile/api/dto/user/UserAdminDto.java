package com.mobile.api.dto.user;

import com.mobile.api.dto.BaseAdminDto;
import com.mobile.api.dto.account.AccountAdminDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAdminDto extends BaseAdminDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "account")
    private AccountAdminDto account;

    @Schema(description = "gender")
    private Integer gender;

    @Schema(description = "birthday")
    private LocalDateTime birthday;
}

package com.mobile.base.dto.user;

import com.mobile.base.dto.account.AccountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    @Schema(description = "account")
    private AccountDto account;

    @Schema(description = "gender")
    private Integer gender;

    @Schema(description = "birthday")
    private LocalDateTime birthday;
}

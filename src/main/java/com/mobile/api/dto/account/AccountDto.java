package com.mobile.api.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AccountDto {
    @Schema(description = "username")
    private String username;

    @Schema(description = "email")
    private String email;

    @Schema(description = "phone number")
    private String phone;

    @Schema(description = "avatar path")
    private String avatarPath;
}

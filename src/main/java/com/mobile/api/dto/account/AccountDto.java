package com.mobile.api.dto.account;

import com.mobile.api.dto.file.FileDto;
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

    @Schema(description = "avatar")
    private FileDto avatar;
}

package com.mobile.api.dto.wallet;

import com.mobile.api.dto.file.FileDto;
import com.mobile.api.dto.user.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
public class WalletDto {
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "User")
    private UserDto user;

    @Schema(description = "Balance")
    private Double balance;

    @Schema(description = "Icon")
    private FileDto icon;

    @Schema(description = "created date")
    private Instant createdDate;

    @Schema(description = "modified date")
    private Instant modifiedDate;

    @Schema(description = "status")
    private Integer status;
}

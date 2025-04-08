package com.mobile.api.dto.period;

import com.mobile.api.dto.wallet.WalletDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
public class PeriodDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "wallet")
    private WalletDto wallet;

    @Schema(description = "startDate")
    private Instant startDate;

    @Schema(description = "endDate")
    private Instant endDate;
}

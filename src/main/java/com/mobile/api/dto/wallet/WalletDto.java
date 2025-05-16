package com.mobile.api.dto.wallet;

import com.mobile.api.dto.currency.CurrencyDto;
import com.mobile.api.dto.file.FileDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class WalletDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "balance")
    private BigDecimal balance;

    @Schema(description = "currency")
    private CurrencyDto currency;

    @Schema(description = "isPrimary")
    private Boolean isPrimary = false;

    @Schema(description = "turnOnNotifications")
    private Boolean turnOnNotifications = true;

    @Schema(description = "chargeToTotal")
    private Boolean chargeToTotal = true;

    @Schema(description = "icon")
    private FileDto icon;

    @Schema(description = "createdDate")
    private Instant createdDate;
}

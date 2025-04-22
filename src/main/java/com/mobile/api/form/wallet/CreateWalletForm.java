package com.mobile.api.form.wallet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeDouble;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Create Wallet Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateWalletForm {
    @Schema(description = "Name", example = "Family", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Name")
    private String name;

    @Schema(description = "Currency ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Currency ID can not be null")
    private Long currencyId;

    @Schema(description = "Is primary", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Is primary can not be null")
    private Boolean isPrimary;

    @Schema(description = "Turn on notifications", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Turn on notifications can not be null")
    private Boolean turnOnNotifications;

    @Schema(description = "Charge to total", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Charge to total can not be null")
    private Boolean chargeToTotal;

    @Schema(description = "Icon ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Icon ID can not be null")
    private Long iconId;
}

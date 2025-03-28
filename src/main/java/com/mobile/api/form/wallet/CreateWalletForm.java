package com.mobile.api.form.wallet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Create Wallet Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateWalletForm {
    @Schema(description = "Balance", example = "1000000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Balance can not be null")
    private Double balance;

    @Schema(description = "Icon Path", example = "/image", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Icon Path")
    private String iconPath;
}

package com.mobile.api.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Resend OTP Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResendOtpForm {
    @Schema(description = "Token", example = "AbcDxy...", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Token")
    private String token;
}

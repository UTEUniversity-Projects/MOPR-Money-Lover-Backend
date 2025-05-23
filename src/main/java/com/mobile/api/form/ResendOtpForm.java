package com.mobile.api.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(description = "Resend OTP Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResendOtpForm {
    @Schema(description = "Token", example = "aBcDxy", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Token cannot be empty")
    private String token;
}

package com.mobile.api.form.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(description = "Registration Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationForm {
    @Schema(description = "OTP", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "OTP cannot be empty")
    private String otp;

    @Schema(description = "Token", example = "AbcDxy...", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Token can not be empty")
    private String token;
}

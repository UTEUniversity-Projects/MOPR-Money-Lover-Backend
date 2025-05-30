package com.mobile.api.form.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.Password;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(description = "Reset Password Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResetPasswordForm {
    @Schema(description = "OTP", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "OTP")
    private String otp;

    @Schema(description = "Password (must be at least 8 characters, include uppercase, lowercase, digit, and special character)",
            example = "P@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
    @Password(allowNull = false)
    private String newPassword;

    @Schema(description = "Token", example = "aBcDxy", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Token cannot be empty")
    private String token;
}

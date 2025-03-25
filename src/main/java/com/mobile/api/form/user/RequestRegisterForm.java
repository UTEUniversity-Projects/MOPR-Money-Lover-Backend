package com.mobile.api.form.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.EmailAddress;
import com.mobile.api.validation.Password;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(description = "Request Register Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRegisterForm {
    @Schema(description = "Email", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @EmailAddress(allowNull = false)
    private String email;

    @Schema(description = "Username", example = "user", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Username")
    private String username;

    @Schema(description = "Password (must be at least 8 characters, include uppercase, lowercase, digit, and special character)",
            example = "P@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
    @Password(allowNull = false)
    private String password;

    @Schema(description = "reCAPTCHA response", example = "xyz", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "reCAPTCHA response can not be empty")
    private String recaptchaResponse;
}

package com.mobile.api.form.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.Password;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request Update Password Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestUpdatePasswordForm {
    @Schema(description = "Old password", example = "OldP@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Old password")
    private String oldPassword;

    @Schema(description = "Password (must be at least 8 characters, include uppercase, lowercase, digit, and special character)",
            example = "P@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
    @Password(allowNull = false)
    private String newPassword;
}

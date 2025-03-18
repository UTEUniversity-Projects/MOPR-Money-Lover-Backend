package com.mobile.base.form.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.base.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Update Password Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePasswordForm {
    @Schema(description = "Password (must be at least 8 characters, include uppercase, lowercase, digit, and special character)",
            example = "P@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
    @Password(allowNull = false)
    private String password;

    @Schema(description = "Old password (must be at least 8 characters, include uppercase, lowercase, digit, and special character)",
            example = "OldP@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
    @Password(allowNull = false)
    private String oldPassword;
}

package com.mobile.api.form.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.EmailAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request Update Email Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestUpdateEmailForm {
    @Schema(description = "Old email", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @EmailAddress(allowNull = false)
    private String oldEmail;

    @Schema(description = "New email", example = "client@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @EmailAddress(allowNull = false)
    private String newEmail;
}

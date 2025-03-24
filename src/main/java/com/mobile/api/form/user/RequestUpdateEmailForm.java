package com.mobile.api.form.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(description = "Request Update Email Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestUpdateEmailForm {
    @Schema(description = "Old email", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Old email cannot be empty")
    @Email(message = "Invalid old email format")
    private String oldEmail;

    @Schema(description = "New email", example = "client@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "New email cannot be empty")
    @Email(message = "Invalid new email format")
    private String newEmail;
}

package com.mobile.api.form.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(description = "Update Email Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateEmailForm {
    @Schema(description = "OTP", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "OTP")
    private String otp;

    @Schema(description = "Token", example = "AbcDxy...", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Token cannot be empty")
    private String token;
}

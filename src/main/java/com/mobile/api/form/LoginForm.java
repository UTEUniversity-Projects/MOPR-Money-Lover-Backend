package com.mobile.api.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(description = "Login Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginForm {
    @Schema(description = "Username", example = "username", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Schema(description = "Password", example = "Abc@1234", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}

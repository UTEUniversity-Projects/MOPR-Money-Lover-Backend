package com.mobile.api.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Login Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginForm {
    @Schema(description = "Email", example = "abc@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Email")
    private String email;

    @Schema(description = "Password", example = "Abc@1234", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Password")
    private String password;
}

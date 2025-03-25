package com.mobile.api.form.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.PhoneNumber;
import com.mobile.api.validation.TypeString;
import com.mobile.api.validation.UserGender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
@Schema(description = "Update User Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserForm {
    @Schema(description = "Username", example = "username", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Username")
    private String username;

    @Schema(description = "Phone number (10-15 digits)", example = "0987654321", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @PhoneNumber(allowNull = true)
    private String phone;

    @Schema(description = "Avatar path", example = "/image/avatar/user1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String avatarPath;

    @Schema(description = "Full name", example = "Nguyễn Văn An", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fullName;

    @Schema(description = "Gender", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @UserGender(allowNull = true)
    private Integer gender;

    @Schema(description = "Birthday", example = "2000-03-25T00:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Instant birthday;
}

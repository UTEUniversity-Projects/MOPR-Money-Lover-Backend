package com.mobile.api.form.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.EmailAddress;
import com.mobile.api.validation.PhoneNumber;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Update Account Admin Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateAccountAdminForm {
    @Schema(description = "ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID cannot be null")
    private Long id;

    @Schema(description = "Username", example = "username", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Username")
    private String username;

    @Schema(description = "Email", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @EmailAddress(allowNull = false)
    private String email;

    @Schema(description = "Phone number (10-15 digits)", example = "0987654321", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @PhoneNumber(allowNull = true)
    private String phone;

    @Schema(description = "Avatar path", example = "/image/avatar/user1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String avatarPath;

    @Schema(description = "Group ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Group ID can not be null")
    private Long groupId;
}

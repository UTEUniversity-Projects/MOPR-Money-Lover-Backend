package com.mobile.api.form.currency;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Create Currency Admin Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCurrencyAdminForm {
    @Schema(description = "Name", example = "Dollar", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Name")
    private String name;

    @Schema(description = "Code", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Code")
    private String code;

    @Schema(description = "Icon ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Icon ID can not be null")
    private Long iconId;
}

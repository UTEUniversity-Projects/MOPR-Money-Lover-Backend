package com.mobile.api.dto.currency;

import com.mobile.api.dto.BaseAdminDto;
import com.mobile.api.dto.file.FileDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CurrencyAdminDto extends BaseAdminDto {
    @Schema(description = "name")
    private String name;

    @Schema(description = "code")
    private String code;

    @Schema(description = "icon")
    private FileDto icon;
}

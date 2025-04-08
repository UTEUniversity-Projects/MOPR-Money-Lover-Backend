package com.mobile.api.dto.currency;

import com.mobile.api.dto.file.FileDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CurrencyDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "code")
    private String code;

    @Schema(description = "icon")
    private FileDto icon;
}

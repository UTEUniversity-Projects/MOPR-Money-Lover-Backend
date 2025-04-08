package com.mobile.api.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TagDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;
}

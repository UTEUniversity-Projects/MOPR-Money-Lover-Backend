package com.mobile.api.dto.category;

import com.mobile.api.dto.file.FileDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategoryDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "description")
    private String description;

    @Schema(description = "isExpense")
    private Boolean isExpense;

    @Schema(description = "icon")
    private FileDto icon;
}

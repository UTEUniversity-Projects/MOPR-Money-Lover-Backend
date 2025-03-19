package com.mobile.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseAdminDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "created date")
    private LocalDateTime createdDate;

    @Schema(description = "modified date")
    private LocalDateTime modifiedDate;
}

package com.mobile.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
public class BaseAdminDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "created date")
    private Instant createdDate;

    @Schema(description = "modified date")
    private Instant modifiedDate;

    @Schema(description = "status")
    private Integer status;
}

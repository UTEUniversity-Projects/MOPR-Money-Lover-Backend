package com.mobile.api.dto.event;

import com.mobile.api.dto.file.FileDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
public class EventDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "walletId")
    private Long walletId;

    @Schema(description = "name")
    private String name;

    @Schema(description = "description")
    private String description;

    @Schema(description = "startDate")
    private Instant startDate;

    @Schema(description = "endDate")
    private Instant endDate;

    @Schema(description = "isCompleted")
    private Boolean isCompleted;

    @Schema(description = "icon")
    private FileDto icon;
}

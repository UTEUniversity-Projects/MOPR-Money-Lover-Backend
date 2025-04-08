package com.mobile.api.dto.reminder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
public class ReminderDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "time")
    private Instant time;
}

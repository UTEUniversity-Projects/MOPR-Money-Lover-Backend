package com.mobile.api.form.reminder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
@Schema(description = "Update Reminder Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateReminderForm {
    @Schema(description = "ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID can not be null")
    private Long id;

    @Schema(description = "Time", example = "2023-10-01T00:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Time can not be null")
    @Future(message = "Time must be in the future")
    private Instant time;
}

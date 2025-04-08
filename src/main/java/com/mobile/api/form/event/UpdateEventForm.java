package com.mobile.api.form.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
@Schema(description = "Update Event Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateEventForm {
    @Schema(description = "ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID can not be null")
    private Long id;

    @Schema(description = "Name", example = "Birthday Party", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Name")
    private String name;

    @Schema(description = "Description", example = "Birthday Party", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeString(fieldName = "Description")
    private String description;

    @Schema(description = "Start Date", example = "2023-10-01T00:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
    @Future(message = "Start date must be in the future")
    @NotNull(message = "Start date can not be null")
    private Instant startDate;

    @Schema(description = "End Date", example = "2023-10-01T00:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
    @Future(message = "End date must be in the future")
    @NotNull(message = "End date can not be null")
    private Instant endDate;

    @Schema(description = "Icon ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Icon ID can not be null")
    private Long iconId;
}

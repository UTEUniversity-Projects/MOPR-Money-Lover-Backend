package com.mobile.api.form.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Update Event Completed Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateEventCompletedForm {
    @Schema(description = "ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID can not be null")
    private Long id;

    @Schema(description = "Is Completed", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Is Completed can not be null")
    private Boolean isCompleted;
}

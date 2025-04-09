package com.mobile.api.form.bill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeDouble;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Update Bill Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateBillForm {
    @Schema(description = "ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID cannot be null")
    private Long id;

    @Schema(description = "Amount", example = "100000", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeDouble(fieldName = "Amount")
    private Double amount;

    @Schema(description = "Note", example = "Grocery shopping", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TypeString(fieldName = "Note", allowNull = true)
    private String note;

    @Schema(description = "Is Included in Report", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Is Included in Report cannot be null")
    private Boolean isIncludedReport;

    @Schema(description = "Category ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

    @Schema(description = "Tag IDs", example = "[1, 2, 3]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<Long> tagIds;

    @Schema(description = "Event ID", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long eventId;

    @Schema(description = "Reminder ID", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long reminderId;

    @Schema(description = "Picture ID", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pictureId;
}

package com.mobile.api.form.bill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.TypeBigDecimal;
import com.mobile.api.validation.TypeDouble;
import com.mobile.api.validation.TypeString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Schema(description = "Create Bill Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBillForm {
    @Schema(description = "Amount", example = "100000", requiredMode = Schema.RequiredMode.REQUIRED)
    @TypeBigDecimal(fieldName = "Amount")
    private BigDecimal amount;

    @Schema(description = "Date", example = "2023-10-01T00:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Date can not be null")
    private Instant date;

    @Schema(description = "Note", example = "Grocery shopping", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TypeString(fieldName = "Note", allowNull = true)
    private String note;

    @Schema(description = "Is Included in Report", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Is Included in Report cannot be null")
    private Boolean isIncludedReport;

    @Schema(description = "Wallet ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Wallet ID cannot be null")
    private Long walletId;

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

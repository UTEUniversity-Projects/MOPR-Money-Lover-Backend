package com.mobile.api.dto.bill;

import com.mobile.api.dto.category.CategoryDto;
import com.mobile.api.dto.event.EventDto;
import com.mobile.api.dto.file.FileDto;
import com.mobile.api.dto.reminder.ReminderDto;
import com.mobile.api.dto.tag.TagDto;
import com.mobile.api.dto.wallet.WalletDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class BillDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "amount")
    private Double amount;

    @Schema(description = "date")
    private Instant date;

    @Schema(description = "note")
    private String note;

    @Schema(description = "isIncludedReport")
    private Boolean isIncludedReport;

    @Schema(description = "wallet")
    private WalletDto wallet;

    @Schema(description = "category")
    private CategoryDto category;

    @Schema(description = "tags")
    private List<TagDto> tags;

    @Schema(description = "event")
    private EventDto event;

    @Schema(description = "reminder")
    private ReminderDto reminder;

    @Schema(description = "picture")
    private FileDto picture;
}

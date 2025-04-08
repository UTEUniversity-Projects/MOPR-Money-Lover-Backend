package com.mobile.api.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
public class NotificationDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "content")
    private String content;

    @Schema(description = "isRead")
    private Boolean isRead;

    @Schema(description = "type")
    private Integer type;

    @Schema(description = "scope")
    private Integer scope;

    @Schema(description = "createdAt")
    private Instant createdAt;
}

package com.mobile.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiMessageDto<T> {
    @Schema(description = "result")
    private boolean result = true;

    @Schema(description = "code")
    private String code = null;

    @Schema(description = "data")
    private T data = null;

    @Schema(description = "message")
    private String message = null;
}

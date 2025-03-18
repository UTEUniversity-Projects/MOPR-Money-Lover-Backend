package com.mobile.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "content")
    private List<T> content;

    @Schema(description = "total elements")
    private long totalElements;

    @Schema(description = "total pages")
    private int totalPages;
}

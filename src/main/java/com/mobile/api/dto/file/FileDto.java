package com.mobile.api.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FileDto {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "fileName")
    private String fileName;

    @Schema(description = "fileUrl")
    private String fileUrl;

    @Schema(description = "fileType")
    private String fileType;
}

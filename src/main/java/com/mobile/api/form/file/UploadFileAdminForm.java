package com.mobile.api.form.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.FileType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Schema(description = "Upload File Admin Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadFileAdminForm {
    @Schema(description = "Files", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Files can not be null")
    private List<MultipartFile> files;

    @Schema(description = "File Type", example = "IMAGE", requiredMode = Schema.RequiredMode.REQUIRED)
    @FileType(allowNull = false)
    private String fileType;

    @Schema(description = "File Type", example = "IMAGE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Scope can not be empty")
    private String scope;

    @Schema(description = "Is system file", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Is system file can not be null")
    private Boolean isSystemFile;
}

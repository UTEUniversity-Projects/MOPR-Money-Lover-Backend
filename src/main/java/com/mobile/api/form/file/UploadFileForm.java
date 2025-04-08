package com.mobile.api.form.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mobile.api.validation.FileType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "Upload File Form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadFileForm {
    @Schema(description = "File", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "File can not be null")
    private MultipartFile file;

    @Schema(description = "File Type", example = "IMAGE", requiredMode = Schema.RequiredMode.REQUIRED)
    @FileType(allowNull = false)
    private String fileType;
}

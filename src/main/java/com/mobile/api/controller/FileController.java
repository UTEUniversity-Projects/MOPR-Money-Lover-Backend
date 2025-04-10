package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.file.FileDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.file.UploadFileForm;
import com.mobile.api.mapper.FileMapper;
import com.mobile.api.model.entity.File;
import com.mobile.api.repository.jpa.FileRepository;
import com.mobile.api.service.FileService;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/file")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FileController extends BaseController {
    @Autowired
    private FileService fileService;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileRepository fileRepository;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<FileDto> uploadFile(
            @Valid @RequestBody UploadFileForm uploadFileForm
    ) throws IOException {
        File uploadedFile = fileService.uploadFile(uploadFileForm.getFile(), uploadFileForm.getFileType());
        return ApiMessageUtils.success(fileMapper.fromEntityToFileDto(uploadedFile), "Upload file successfully");
    }

    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Transactional
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id) throws IOException {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));

        if (Objects.equals(file.getIsSystemFile(), true) && !getIsSuperAdmin()) {
            throw new BusinessException(ErrorCode.BUSINESS_PERMISSION_DENIED);
        }

        return fileService.downloadFile(id);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> deleteFile(@PathVariable Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));

        if (Objects.equals(file.getIsSystemFile(), true) && !getIsSuperAdmin()) {
            throw new BusinessException(ErrorCode.BUSINESS_PERMISSION_DENIED);
        }

        fileService.deleteFile(id);
        return ApiMessageUtils.success(null, "Delete file successfully");
    }
}

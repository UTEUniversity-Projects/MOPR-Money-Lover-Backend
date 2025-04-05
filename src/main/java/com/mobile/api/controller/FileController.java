package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.file.FileDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.mapper.FileMapper;
import com.mobile.api.model.entity.File;
import com.mobile.api.service.FileService;
import com.mobile.api.utils.ApiMessageUtils;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/file")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FileController extends BaseController {
    @Autowired
    private FileService fileService;
    @Autowired
    private FileMapper fileMapper;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<FileDto> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("fileType") String fileType) throws IOException {
        File uploadedFile = fileService.uploadFile(file, fileType);
        return ApiMessageUtils.success(fileMapper.fromEntityToFileDto(uploadedFile), "Upload file successfully");
    }

    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Transactional
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id) throws IOException {
        return fileService.downloadFile(id);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Hidden
    public ApiMessageDto<String> deleteFile(@PathVariable Long id) {
        if (!getIsSuperAdmin()) {
            throw new BusinessException(ErrorCode.BUSINESS_NO_PERMISSION);
        }

        fileService.deleteFile(id);
        return ApiMessageUtils.success(null, "Delete file successfully");
    }
}

package com.mobile.api.controller;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.event.EventDto;
import com.mobile.api.dto.file.FileDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.file.UploadFileAdminForm;
import com.mobile.api.form.file.UploadFileForm;
import com.mobile.api.mapper.FileMapper;
import com.mobile.api.model.criteria.EventCriteria;
import com.mobile.api.model.criteria.FileCriteria;
import com.mobile.api.model.entity.Event;
import com.mobile.api.model.entity.File;
import com.mobile.api.repository.jpa.FileRepository;
import com.mobile.api.service.FileService;
import com.mobile.api.utils.ApiMessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('FIL_LIS')")
    public ApiMessageDto<PaginationDto<FileDto>> getFileList(
            @Valid @ModelAttribute FileCriteria fileCriteria,
            Pageable pageable
    ) {
        Specification<File> specification = fileCriteria.getSpecification();
        Page<File> page = fileRepository.findAll(specification, pageable);

        PaginationDto<FileDto> responseDto = new PaginationDto<>(
                fileMapper.fromEntitiesToFileDto(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List files successfully");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('FIL_GET')")
    public ApiMessageDto<FileDto> getFile(@PathVariable Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EVENT_NOT_FOUND));

        return ApiMessageUtils.success(fileMapper.fromEntityToFileDto(file), "Get file successfully");
    }

    @PostMapping(value = "/client/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<FileDto> uploadFile(
            @Valid @ModelAttribute UploadFileForm uploadFileForm
    ) throws IOException {
        File file = fileService.uploadFile(uploadFileForm.getFile(), uploadFileForm.getFileType(), false, null);
        return ApiMessageUtils.success(fileMapper.fromEntityToFileDto(fileRepository.save(file)), "Upload file successfully");
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('FIL_UPL')")
    @Transactional
    @Operation(summary = "Attention", description = "You can only upload a maximum of 10 files at a time")
    public ApiMessageDto<List<FileDto>> uploadFileAdmin(
            @Valid @ModelAttribute UploadFileAdminForm uploadFileAdminForm
    ) throws IOException {
        if (uploadFileAdminForm.getFiles().size() > BaseConstant.FILE_ELEMENTS_MAXIMUM) {
            throw new BusinessException(ErrorCode.BUSINESS_ELEMENTS_EXCEEDED_LIMIT);
        }

        List<File> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : uploadFileAdminForm.getFiles()) {
            File uploaded = fileService.uploadFile(
                    file, uploadFileAdminForm.getFileType(), uploadFileAdminForm.getIsSystemFile(), uploadFileAdminForm.getScope()
            );
            uploadedFiles.add(uploaded);
        }

        List<File> savedFiles = fileRepository.saveAll(uploadedFiles);
        return ApiMessageUtils.success(fileMapper.fromEntitiesToFileDto(savedFiles), "Upload files successfully");
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

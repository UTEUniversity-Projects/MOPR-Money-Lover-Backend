package com.mobile.api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.model.entity.File;
import com.mobile.api.repository.FileRepository;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FileService {
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ServletContext servletContext;

    @Value("${file.max-size}")
    private long maxFileSize;

    @Value("#{'${file.allowed-types.image}'.split(',')}")
    private List<String> allowedImageTypes;

    @Value("#{'${file.allowed-types.video}'.split(',')}")
    private List<String> allowedVideoTypes;

    @Value("#{'${file.allowed-types.audio}'.split(',')}")
    private List<String> allowedAudioTypes;

    @Value("#{'${file.allowed-types.document}'.split(',')}")
    private List<String> allowedDocumentTypes;

    @Value("#{'${file.allowed-types.archive}'.split(',')}")
    private List<String> allowedArchiveTypes;

    @Value("#{'${file.allowed-types.executable}'.split(',')}")
    private List<String> allowedExecutableTypes;

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean isAllowedFileType(String extension, String fileType) {
        return switch (fileType) {
            case "image" -> allowedImageTypes.contains(extension);
            case "video" -> allowedVideoTypes.contains(extension);
            case "audio" -> allowedAudioTypes.contains(extension);
            case "document" -> allowedDocumentTypes.contains(extension);
            case "archive" -> allowedArchiveTypes.contains(extension);
            case "executable" -> allowedExecutableTypes.contains(extension);
            default -> false;
        };
    }

    public File uploadFile(MultipartFile multipartFile, String fileType) throws IOException {
        if (multipartFile.getSize() > maxFileSize) {
            throw new BusinessException(ErrorCode.FILE_SIZE_EXCEEDED);
        }

        String extension = getFileExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        if (!isAllowedFileType(extension, fileType)) {
            throw new BusinessException(ErrorCode.FILE_TYPE_NOT_SUPPORTED);
        }

        // Upload file to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
        String url = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        // Save file information to database
        File file = new File();
        file.setFileName(multipartFile.getOriginalFilename());
        file.setFileUrl(url);
        file.setFileType(fileType);
        file.setFileSize(multipartFile.getSize());
        file.setPublicId(publicId);

        return fileRepository.save(file);
    }

    public void deleteFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));

        // Delete file from Cloudinary
        try {
            cloudinary.uploader().destroy(file.getPublicId(), ObjectUtils.emptyMap());
            fileRepository.delete(file);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public ResponseEntity<InputStreamResource> downloadFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FILE_NOT_FOUND));

        // Get file from Cloudinary
        String fileUrl = file.getFileUrl();
        byte[] fileBytes = restTemplate.getForObject(fileUrl, byte[].class);
        if (fileBytes == null) {
            throw new BusinessException(ErrorCode.FILE_DOWNLOAD_ERROR_WITH_THIRD_PARTY);
        }

        InputStream inputStream = new ByteArrayInputStream(fileBytes);
        InputStreamResource resource = new InputStreamResource(inputStream);

        // Identify content type
        String contentType = servletContext.getMimeType(file.getFileName());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(90, TimeUnit.DAYS).cachePublic()) // Allow caching for 90 days
                .eTag("\"" + file.getId() + "-" + file.getModifiedDate() + "\"") // ETag based on file ID and modified date
                .lastModified(file.getModifiedDate().toEpochMilli()) // Last-Modified based on the file's modified date
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }
}

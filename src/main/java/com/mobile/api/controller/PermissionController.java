package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.permission.PermissionAdminDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.permission.CreatePermissionAdminForm;
import com.mobile.api.form.permission.UpdatePermissionAdminForm;
import com.mobile.api.mapper.PermissionMapper;
import com.mobile.api.model.criteria.PermissionCriteria;
import com.mobile.api.model.entity.Permission;
import com.mobile.api.repository.PermissionRepository;
import com.mobile.api.utils.ApiMessageUtils;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/permission")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Hidden
public class PermissionController extends BaseController {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PermissionMapper permissionMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('PER_LIS')")
    public ApiMessageDto<PaginationDto<PermissionAdminDto>> getPermissionList(
            @Valid @ModelAttribute PermissionCriteria permissionCriteria,
            Pageable pageable
    ) {
        Specification<Permission> specification = permissionCriteria.getSpecification();
        Page<Permission> page = permissionRepository.findAll(specification, pageable);

        PaginationDto<PermissionAdminDto> responseDto = new PaginationDto<>(
                permissionMapper.fromEntitiesToPermissionAdminDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List permissions successfully");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('PER_GET')")
    public ApiMessageDto<PermissionAdminDto> getPermission(@PathVariable Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND));

        return ApiMessageUtils.success(permissionMapper.fromEntityToPermissionAdminDto(permission), "Get permission successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('PER_CRE')")
    public ApiMessageDto<Void> createPermission(@Valid @RequestBody CreatePermissionAdminForm createPermissionAdminForm) {
        if (!getIsSuperAdmin()) {
            throw new BusinessException(ErrorCode.BUSINESS_NO_PERMISSION);
        }

        Optional<Permission> existingPermission = permissionRepository.findByActionOrCode(
                createPermissionAdminForm.getAction(), createPermissionAdminForm.getCode()
        );
        if (existingPermission.isPresent()) {
            if (existingPermission.get().getAction().equals(createPermissionAdminForm.getAction())) {
                throw new BusinessException(ErrorCode.PERMISSION_ACTION_EXISTED);
            }
            if (existingPermission.get().getCode().equals(createPermissionAdminForm.getCode())) {
                throw new BusinessException(ErrorCode.PERMISSION_CODE_EXISTED);
            }
        }

        Permission permission = permissionMapper.fromCreatePermissionAdminFormToEntity(createPermissionAdminForm);
        permissionRepository.save(permission);

        return ApiMessageUtils.success(null, "Create permission successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('PER_UPD')")
    public ApiMessageDto<Void> updatePermission(@Valid @RequestBody UpdatePermissionAdminForm updatePermissionAdminForm) {
        if (!getIsSuperAdmin()) {
            throw new BusinessException(ErrorCode.BUSINESS_NO_PERMISSION);
        }

        Permission permission = permissionRepository.findById(updatePermissionAdminForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!Objects.equals(permission.getAction(), updatePermissionAdminForm.getAction())) {
            if (permissionRepository.existsByAction(updatePermissionAdminForm.getAction())) {
                throw new BusinessException(ErrorCode.PERMISSION_ACTION_EXISTED);
            }
        }
        if (!Objects.equals(permission.getCode(), updatePermissionAdminForm.getCode())) {
            if (permissionRepository.existsByCode(updatePermissionAdminForm.getCode())) {
                throw new BusinessException(ErrorCode.PERMISSION_CODE_EXISTED);
            }
        }

        permissionMapper.updateFromUpdatePermissionAdminForm(permission, updatePermissionAdminForm);
        permissionRepository.save(permission);

        return ApiMessageUtils.success(null, "Update permission successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('PER_DEL')")
    public ApiMessageDto<Void> deletePermission(@PathVariable Long id) {
        if (!getIsSuperAdmin()) {
            throw new BusinessException(ErrorCode.BUSINESS_NO_PERMISSION);
        }

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND));

        permission.getGroups().clear();
        permissionRepository.save(permission);
        permissionRepository.deleteById(id);
        return ApiMessageUtils.success(null, "Delete permission successfully");
    }
}

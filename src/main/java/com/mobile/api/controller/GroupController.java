package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.group.GroupAdminDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.group.CreateGroupAdminForm;
import com.mobile.api.form.group.UpdateGroupAdminForm;
import com.mobile.api.form.group.UpdatePermissionListForm;
import com.mobile.api.mapper.GroupMapper;
import com.mobile.api.model.criteria.GroupCriteria;
import com.mobile.api.model.entity.Group;
import com.mobile.api.model.entity.Permission;
import com.mobile.api.repository.AccountRepository;
import com.mobile.api.repository.GroupRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/group")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Hidden
public class GroupController extends BaseController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupMapper groupMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('GRO_LIS')")
    public ApiMessageDto<PaginationDto<GroupAdminDto>> getGroupList(
            @Valid @ModelAttribute GroupCriteria groupCriteria,
            Pageable pageable
    ) {
        Specification<Group> specification = groupCriteria.getSpecification();
        Page<Group> page = groupRepository.findAll(specification, pageable);

        PaginationDto<GroupAdminDto> responseDto = new PaginationDto<>(
                groupMapper.fromEntitiesToGroupAdminDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List groups successfully");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('GRO_GET')")
    public ApiMessageDto<GroupAdminDto> getGroup(@PathVariable Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND));

        return ApiMessageUtils.success(groupMapper.fromEntityToGroupAdminDto(group), "Get group successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('GRO_CRE')")
    public ApiMessageDto<Void> createGroup(@Valid @RequestBody CreateGroupAdminForm createGroupAdminForm) {
        if (!getIsSuperAdmin()) {
            throw new BusinessException(ErrorCode.BUSINESS_PERMISSION_DENIED);
        }
        if (groupRepository.existsByName(createGroupAdminForm.getName())) {
            throw new BusinessException(ErrorCode.GROUP_NAME_EXISTED);
        }

        Group group = groupMapper.fromCreateGroupAdminFormToEntity(createGroupAdminForm);
        groupRepository.save(group);

        return ApiMessageUtils.success(null, "Create group successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('GRO_UPD')")
    public ApiMessageDto<Void> updateGroup(@Valid @RequestBody UpdateGroupAdminForm updateGroupAdminForm) {
        if (!getIsSuperAdmin()) {
            throw new BusinessException(ErrorCode.BUSINESS_PERMISSION_DENIED);
        }

        Group group = groupRepository.findById(updateGroupAdminForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND));

        if (!Objects.equals(group.getName(), updateGroupAdminForm.getName())) {
            if (groupRepository.existsByName(updateGroupAdminForm.getName())) {
                throw new BusinessException(ErrorCode.GROUP_NAME_EXISTED);
            }
        }

        groupMapper.updateFromUpdateGroupAdminForm(group, updateGroupAdminForm);
        groupRepository.save(group);

        return ApiMessageUtils.success(null, "Update group successfully");
    }

    @PutMapping(value = "/add-permissions", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('GRO_PER_ADD')")
    public ApiMessageDto<Void> addPermissionsToGroup(@Valid @RequestBody UpdatePermissionListForm updatePermissionListForm) {
        if (!getIsSuperAdmin()) {
            throw new BusinessException(ErrorCode.BUSINESS_PERMISSION_DENIED);
        }

        Group group = groupRepository.findById(updatePermissionListForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND));

        List<Permission> permissionsToAdd = permissionRepository.findAllById(Arrays.asList(updatePermissionListForm.getPermissions()));
        if (permissionsToAdd.size() != updatePermissionListForm.getPermissions().length) {
            throw new BusinessException(ErrorCode.PERMISSION_NOT_FOUND);
        }

        group.getPermissions().addAll(permissionsToAdd);
        groupRepository.save(group);

        return ApiMessageUtils.success(null, "Permissions added successfully");
    }

    @PutMapping(value = "/remove-permissions", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('GRO_PER_REM')")
    public ApiMessageDto<Void> removePermissionsFromGroup(@Valid @RequestBody UpdatePermissionListForm updatePermissionListForm) {
        if (!getIsSuperAdmin()) {
            throw new BusinessException(ErrorCode.BUSINESS_PERMISSION_DENIED);
        }

        Group group = groupRepository.findById(updatePermissionListForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND));

        List<Long> permissionIdsToRemove = Arrays.asList(updatePermissionListForm.getPermissions());
        group.getPermissions().removeIf(permission -> permissionIdsToRemove.contains(permission.getId()));
        groupRepository.save(group);

        return ApiMessageUtils.success(null, "Permissions removed successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('GRO_DEL')")
    public ApiMessageDto<Void> deleteGroup(@PathVariable Long id) {
        if (!getIsSuperAdmin()) {
            throw new BusinessException(ErrorCode.BUSINESS_PERMISSION_DENIED);
        }
        if (accountRepository.countByGroupId(id) > 0) {
            throw new BusinessException(ErrorCode.GROUP_CANT_DELETE);
        }

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND));

        group.getPermissions().clear();
        groupRepository.save(group);
        groupRepository.deleteById(id);
        return ApiMessageUtils.success(null, "Delete group successfully");
    }
}

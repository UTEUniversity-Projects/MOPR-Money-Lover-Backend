package com.mobile.api.mapper;

import com.mobile.api.dto.permission.PermissionAdminDto;
import com.mobile.api.form.permission.CreatePermissionAdminForm;
import com.mobile.api.form.permission.UpdatePermissionAdminForm;
import com.mobile.api.model.entity.Permission;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {})
public interface PermissionMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "action", target = "action")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "nameGroup", target = "nameGroup")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "showMenu", target = "showMenu")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreatePermissionAdminFormToEntity")
    Permission fromCreatePermissionAdminFormToEntity(CreatePermissionAdminForm createPermissionAdminForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "action", target = "action")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "nameGroup", target = "nameGroup")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "showMenu", target = "showMenu")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdatePermissionAdminForm")
    void updateFromUpdatePermissionAdminForm(@MappingTarget Permission permission, UpdatePermissionAdminForm updatePermissionAdminForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "action", target = "action")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "nameGroup", target = "nameGroup")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "showMenu", target = "showMenu")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToPermissionAdminDto")
    PermissionAdminDto fromEntityToPermissionAdminDto(Permission permission);

    @IterableMapping(elementTargetType = PermissionAdminDto.class, qualifiedByName = "fromEntityToPermissionAdminDto")
    @Named("fromEntitiesToPermissionAdminDtoList")
    List<PermissionAdminDto> fromEntitiesToPermissionAdminDtoList(List<Permission> permissions);
}

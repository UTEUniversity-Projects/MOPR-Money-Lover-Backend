package com.mobile.base.mapper;

import com.mobile.base.dto.group.GroupAdminDto;
import com.mobile.base.form.group.CreateGroupAdminForm;
import com.mobile.base.form.group.UpdateGroupAdminForm;
import com.mobile.base.model.entity.Group;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PermissionMapper.class})
public interface GroupMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "isSystemGroup", target = "isSystemGroup")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateGroupAdminFormToEntity")
    Group fromCreateGroupAdminFormToEntity(CreateGroupAdminForm createGroupAdminForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "isSystemGroup", target = "isSystemGroup")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateGroupAdminForm")
    void updateFromUpdateGroupAdminForm(@MappingTarget Group group, UpdateGroupAdminForm updateGroupAdminForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "isSystemGroup", target = "isSystemGroup")
    @Mapping(source = "permissions", target = "permissions", qualifiedByName = "fromEntityToPermissionAdminDto")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToGroupAdminDto")
    GroupAdminDto fromEntityToGroupAdminDto(Group group);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "isSystemGroup", target = "isSystemGroup")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToGroupAdminDtoInAccount")
    GroupAdminDto fromEntityToGroupAdminDtoInAccount(Group group);

    @IterableMapping(elementTargetType = GroupAdminDto.class, qualifiedByName = "fromEntityToGroupAdminDto")
    @Named("fromEntitiesToGroupAdminDtoList")
    List<GroupAdminDto> fromEntitiesToGroupAdminDtoList(List<Group> groups);
}

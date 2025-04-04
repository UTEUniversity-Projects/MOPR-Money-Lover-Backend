package com.mobile.api.mapper;

import com.mobile.api.dto.user.UserAdminDto;
import com.mobile.api.dto.user.UserDto;
import com.mobile.api.form.user.UpdateUserAdminForm;
import com.mobile.api.form.user.UpdateUserForm;
import com.mobile.api.model.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class})
public interface UserMapper {
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "birthday", target = "birthday")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateUserAdminForm")
    void updateFromUpdateUserAdminForm(@MappingTarget User user, UpdateUserAdminForm updateUserAdminForm);

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "birthday", target = "birthday")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateUserForm")
    void updateFromUpdateUserForm(@MappingTarget User user, UpdateUserForm updateUserForm);

    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountDto")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "birthday", target = "birthday")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToUserDto")
    UserDto fromEntityToUserDto(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountAdminDto")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToUserAdminDto")
    UserAdminDto fromEntityToUserAdminDto(User user);

    @IterableMapping(elementTargetType = UserAdminDto.class, qualifiedByName = "fromEntityToUserAdminDto")
    List<UserAdminDto> fromEntitiesToUserAdminDtoList(List<User> users);
}

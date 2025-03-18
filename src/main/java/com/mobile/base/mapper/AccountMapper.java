package com.mobile.base.mapper;

import com.mobile.base.dto.account.AccountAdminDto;
import com.mobile.base.dto.account.AccountDto;
import com.mobile.base.form.account.CreateAccountAdminForm;
import com.mobile.base.form.account.CreateAccountForm;
import com.mobile.base.form.account.UpdateAccountAdminForm;
import com.mobile.base.form.account.UpdateAccountForm;
import com.mobile.base.model.entity.Account;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {GroupMapper.class})
public interface AccountMapper {
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateAccountFormToEntity")
    Account fromCreateAccountFormToEntity(CreateAccountForm createAccountForm);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateAccountAdminFormToEntity")
    Account fromCreateAccountAdminFormToEntity(CreateAccountAdminForm createAccountAdminForm);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateAccountForm")
    void updateFromUpdateAccountForm(@MappingTarget Account account, UpdateAccountForm updateAccountForm);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateAccountAdminForm")
    void updateFromUpdateAccountAdminForm(@MappingTarget Account account, UpdateAccountAdminForm updateAccountAdminForm);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToAccountDto")
    AccountDto fromEntityToAccountDto(Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "group", target = "group", qualifiedByName = "fromEntityToGroupAdminDtoInAccount")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToAccountAdminDto")
    AccountAdminDto fromEntityToAccountAdminDto(Account account);

    @IterableMapping(elementTargetType = AccountAdminDto.class, qualifiedByName = "fromEntityToAccountAdminDto")
    @Named("fromEntitiesToAccountAdminDtoList")
    List<AccountAdminDto> fromEntitiesToAccountAdminDtoList(List<Account> accounts);
}

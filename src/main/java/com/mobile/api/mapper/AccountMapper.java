package com.mobile.api.mapper;

import com.mobile.api.dto.account.AccountAdminDto;
import com.mobile.api.dto.account.AccountDto;
import com.mobile.api.model.entity.Account;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {GroupMapper.class, FileMapper.class})
public interface AccountMapper {
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "avatar", target = "avatar", qualifiedByName = "fromEntityToSimpleFileDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToAccountDto")
    AccountDto fromEntityToAccountDto(Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "isSuperAdmin", target = "isSuperAdmin")
    @Mapping(source = "verified", target = "verified")
    @Mapping(source = "avatar", target = "avatar", qualifiedByName = "fromEntityToSimpleFileDto")
    @Mapping(source = "group", target = "group", qualifiedByName = "fromEntityToGroupAdminDtoInAccount")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToAccountAdminDto")
    AccountAdminDto fromEntityToAccountAdminDto(Account account);

    @IterableMapping(elementTargetType = AccountAdminDto.class, qualifiedByName = "fromEntityToAccountAdminDto")
    List<AccountAdminDto> fromEntitiesToAccountAdminDtoList(List<Account> accounts);
}

package com.mobile.api.mapper;

import com.mobile.api.dto.wallet.WalletDto;
import com.mobile.api.form.wallet.CreateWalletForm;
import com.mobile.api.form.wallet.UpdateWalletForm;
import com.mobile.api.model.entity.Wallet;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {FileMapper.class, CurrencyMapper.class})
public interface WalletMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "isPrimary", target = "isPrimary")
    @Mapping(source = "turnOnNotifications", target = "turnOnNotifications")
    @Mapping(source = "chargeToTotal", target = "chargeToTotal")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateWalletFormToEntity")
    Wallet fromCreateWalletFormToEntity(CreateWalletForm form);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "isPrimary", target = "isPrimary")
    @Mapping(source = "turnOnNotifications", target = "turnOnNotifications")
    @Mapping(source = "chargeToTotal", target = "chargeToTotal")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateWalletForm")
    void updateFromUpdateWalletForm(@MappingTarget Wallet wallet, UpdateWalletForm updateWalletForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "currency", target = "currency", qualifiedByName = "fromEntityToCurrencyDto")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "isPrimary", target = "isPrimary")
    @Mapping(source = "turnOnNotifications", target = "turnOnNotifications")
    @Mapping(source = "chargeToTotal", target = "chargeToTotal")
    @Mapping(source = "icon", target = "icon", qualifiedByName = "fromEntityToFileDto")
    @Mapping(source = "createdDate", target = "createdDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToWalletDto")
    WalletDto fromEntityToWalletDto(Wallet wallet);

    @IterableMapping(elementTargetType = WalletDto.class, qualifiedByName = "fromEntityToWalletDto")
    @Named("fromEntitiesToWalletDtoList")
    List<WalletDto> fromEntitiesToWalletDtoList(List<Wallet> wallets);
}

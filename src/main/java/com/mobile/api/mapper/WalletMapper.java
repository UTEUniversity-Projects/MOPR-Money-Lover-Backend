package com.mobile.api.mapper;

import com.mobile.api.dto.wallet.WalletDto;
import com.mobile.api.model.entity.Wallet;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {FileMapper.class})
public interface WalletMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "icon", target = "icon", qualifiedByName = "fromEntityToFileDto")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToWalletDto")
    WalletDto fromEntityToWalletDto(Wallet wallet);

    @IterableMapping(elementTargetType = WalletDto.class, qualifiedByName = "fromEntityToWalletDto")
    @Named("fromEntitiesToWalletDtoList")
    List<WalletDto> fromEntitiesToWalletDtoList(List<Wallet> wallets);
}

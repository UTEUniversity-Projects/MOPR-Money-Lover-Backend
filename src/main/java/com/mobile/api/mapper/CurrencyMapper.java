package com.mobile.api.mapper;

import com.mobile.api.dto.currency.CurrencyAdminDto;
import com.mobile.api.dto.currency.CurrencyDto;
import com.mobile.api.model.entity.Currency;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {FileMapper.class})
public interface CurrencyMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "icon", target = "icon", qualifiedByName = "fromEntityToSimpleFileDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToCurrencyDto")
    CurrencyDto fromEntityToCurrencyDto(Currency currency);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "icon", target = "icon", qualifiedByName = "fromEntityToSimpleFileDto")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToCurrencyAdminDto")
    CurrencyAdminDto fromEntityToCurrencyAdminDto(Currency currency);

    @IterableMapping(elementTargetType = CurrencyDto.class, qualifiedByName = "fromEntityToCurrencyDto")
    @Named("fromEntitiesToCurrencyDtoList")
    List<CurrencyDto> fromEntitiesToCurrencyDtoList(List<Currency> currencies);

    @IterableMapping(elementTargetType = CurrencyAdminDto.class, qualifiedByName = "fromEntityToCurrencyAdminDto")
    @Named("fromEntitiesToCurrencyAdminDtoList")
    List<CurrencyAdminDto> fromEntitiesToCurrencyAdminDtoList(List<Currency> currencies);
}

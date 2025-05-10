package com.mobile.api.mapper;

import com.mobile.api.dto.period.PeriodDto;
import com.mobile.api.model.entity.Period;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {WalletMapper.class})
public interface PeriodMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "wallet", target = "wallet", qualifiedByName = "fromEntityToSimpleWalletDto")
    @Mapping(source = "totalAmount", target = "totalAmount")
    @Mapping(source = "totalSpent", target = "totalSpent")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToPeriodDto")
    PeriodDto fromEntityToPeriodDto(Period period);
}

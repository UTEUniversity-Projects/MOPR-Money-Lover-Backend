package com.mobile.api.mapper;

import com.mobile.api.dto.TokenDto;
import com.mobile.api.model.Token;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {})
public interface TokenMapper {
    @Mapping(source = "token", target = "token")
    @Mapping(source = "kind", target = "kind")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToTokenDto")
    TokenDto fromEntityToTokenDto(Token token);
}

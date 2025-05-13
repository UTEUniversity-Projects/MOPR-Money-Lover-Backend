package com.mobile.api.mapper;

import com.mobile.api.dto.notification.NotificationDto;
import com.mobile.api.model.entity.Notification;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {WalletMapper.class})
public interface NotificationMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "wallet", target = "wallet", qualifiedByName = "fromEntityToSimpleWalletDto")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "isRead", target = "isRead")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "scope", target = "scope")
    @Mapping(source = "createdDate", target = "createdDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToNotificationDto")
    NotificationDto fromEntityToNotificationDto(Notification notification);

    @IterableMapping(elementTargetType = NotificationDto.class, qualifiedByName = "fromEntityToNotificationDto")
    @Named("fromEntitiesToNotificationDtoList")
    List<NotificationDto> fromEntitiesToNotificationDtoList(List<Notification> notifications);
}

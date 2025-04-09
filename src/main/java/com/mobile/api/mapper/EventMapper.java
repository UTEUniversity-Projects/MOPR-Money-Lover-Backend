package com.mobile.api.mapper;

import com.mobile.api.dto.event.EventDto;
import com.mobile.api.form.event.CreateEventForm;
import com.mobile.api.form.event.UpdateEventForm;
import com.mobile.api.model.entity.Event;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {FileMapper.class})
public interface EventMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateEventFormToEntity")
    Event fromCreateEventFormToEntity(CreateEventForm createEventForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateEventForm")
    void updateFromUpdateEventForm(@MappingTarget Event event, UpdateEventForm updateEventForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "wallet.id", target = "walletId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "isCompleted", target = "isCompleted")
    @Mapping(source = "icon", target = "icon", qualifiedByName = "fromEntityToSimpleFileDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToEventDto")
    EventDto fromEntityToEventDto(Event event);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "icon", target = "icon", qualifiedByName = "fromEntityToSimpleFileDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToSimpleEventDto")
    EventDto fromEntityToSimpleEventDto(Event event);

    @IterableMapping(elementTargetType = EventDto.class, qualifiedByName = "fromEntityToEventDto")
    @Named("fromEntitiesToEventDtoList")
    List<EventDto> fromEntitiesToEventDtoList(List<Event> events);
}

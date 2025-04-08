package com.mobile.api.mapper;

import com.mobile.api.dto.reminder.ReminderDto;
import com.mobile.api.form.reminder.CreateReminderForm;
import com.mobile.api.form.reminder.UpdateReminderForm;
import com.mobile.api.model.entity.Reminder;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {})
public interface ReminderMapper {
    @Mapping(source = "time", target = "time")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateReminderFormToEntity")
    Reminder fromCreateReminderFormToEntity(CreateReminderForm createReminderForm);

    @Mapping(source = "time", target = "time")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateReminderFormToEntity")
    void updateFromUpdateReminderFormToEntity(@MappingTarget Reminder reminder, UpdateReminderForm updateReminderForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "time", target = "time")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToReminderDto")
    ReminderDto fromEntityToReminderDto(Reminder reminder);

    @IterableMapping(elementTargetType = ReminderDto.class, qualifiedByName = "fromEntityToReminderDto")
    @Named("fromEntitiesToReminderDtoList")
    List<ReminderDto> fromEntitiesToReminderDtoList(List<Reminder> reminders);
}

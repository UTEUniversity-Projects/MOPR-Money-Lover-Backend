package com.mobile.api.mapper;

import com.mobile.api.dto.bill.BillDto;
import com.mobile.api.form.bill.CreateBillForm;
import com.mobile.api.form.bill.UpdateBillForm;
import com.mobile.api.model.entity.Bill;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {WalletMapper.class, CategoryMapper.class, TagMapper.class, EventMapper.class, ReminderMapper.class, FileMapper.class})
public interface BillMapper {
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "isIncludedReport", target = "isIncludedReport")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateBillFormToEntity")
    Bill fromCreateBillFormToEntity(CreateBillForm createBillForm);

    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "isIncludedReport", target = "isIncludedReport")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateBillForm")
    void updateFromUpdateBillForm(@MappingTarget Bill bill, UpdateBillForm updateBillForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "isIncludedReport", target = "isIncludedReport")
    @Mapping(source = "wallet", target = "wallet", qualifiedByName = "fromEntityToSimpleWalletDto")
    @Mapping(source = "category", target = "category", qualifiedByName = "fromEntityToSimpleCategory")
    @Mapping(source = "tags", target = "tags", qualifiedByName = "fromEntitiesToTagDtoList")
    @Mapping(source = "event", target = "event", qualifiedByName = "fromEntityToSimpleEventDto")
    @Mapping(source = "reminder", target = "reminder", qualifiedByName = "fromEntityToReminderDto")
    @Mapping(source = "picture", target = "picture", qualifiedByName = "fromEntityToSimpleFileDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToBillDto")
    BillDto fromEntityToBillDto(Bill bill);

    @IterableMapping(elementTargetType = BillDto.class, qualifiedByName = "fromEntityToBillDto")
    @Named("fromEntitiesToBillDtoList")
    List<BillDto> fromEntitiesToBillDtoList(List<Bill> bills);
}

package com.mobile.api.mapper;

import com.mobile.api.dto.budget.BudgetDto;
import com.mobile.api.form.budget.CreateBudgetForm;
import com.mobile.api.form.budget.UpdateBudgetForm;
import com.mobile.api.model.entity.Budget;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {WalletMapper.class, CategoryMapper.class})
public interface BudgetMapper {
    @Mapping(source = "amount", target = "amount")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateBudgetFormToEntity")
    Budget fromCreateBudgetFormToEntity(CreateBudgetForm createBudgetForm);

    @Mapping(source = "amount", target = "amount")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateBudgetForm")
    void updateFromUpdateBudgetForm(@MappingTarget Budget budget, UpdateBudgetForm updateBudgetForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "category", target = "category", qualifiedByName = "fromEntityToSimpleCategory")
    @Mapping(source = "wallet", target = "wallet", qualifiedByName = "fromEntityToSimpleWalletDto")
    @Mapping(source = "periodType", target = "periodType")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "amount", target = "amount")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToBudgetDto")
    BudgetDto fromEntityToBudgetDto(Budget budget);

    @IterableMapping(elementTargetType = BudgetDto.class, qualifiedByName = "fromEntityToBudgetDto")
    List<BudgetDto> fromEntitiesToBudgetDtoList(List<Budget> budgets);
}

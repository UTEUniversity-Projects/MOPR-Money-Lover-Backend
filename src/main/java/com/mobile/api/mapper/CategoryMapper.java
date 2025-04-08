package com.mobile.api.mapper;

import com.mobile.api.dto.category.CategoryDto;
import com.mobile.api.form.category.CreateCategoryForm;
import com.mobile.api.form.category.UpdateCategoryForm;
import com.mobile.api.model.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {FileMapper.class})
public interface CategoryMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "isExpense", target = "isExpense")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateCategoryFormToEntity")
    Category fromCreateCategoryFormToEntity(CreateCategoryForm createCategoryForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "isExpense", target = "isExpense")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateCategoryForm")
    void updateFromUpdateCategoryForm(@MappingTarget Category category, UpdateCategoryForm updateCategoryForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "isExpense", target = "isExpense")
    @Mapping(source = "icon", target = "icon", qualifiedByName = "fromEntityToFileDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToCategoryDto")
    CategoryDto fromEntityToCategoryDto(Category category);

    @IterableMapping(elementTargetType = CategoryDto.class, qualifiedByName = "fromEntityToCategoryDto")
    @Named("fromEntitiesToCategoryDtoList")
    List<CategoryDto> fromEntitiesToCategoryDtoList(List<Category> categories);
}

package com.mobile.api.mapper;

import com.mobile.api.dto.tag.TagDto;
import com.mobile.api.form.tag.CreateTagForm;
import com.mobile.api.form.tag.UpdateTagForm;
import com.mobile.api.model.entity.Tag;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {})
public interface TagMapper {
    @Mapping(source = "name", target = "name")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateTagFormToEntity")
    Tag fromCreateTagFormToEntity(CreateTagForm createTagForm);

    @Mapping(source = "name", target = "name")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateTagForm")
    void updateFromUpdateTagForm(@MappingTarget Tag tag, UpdateTagForm updateTagForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToTagDto")
    TagDto fromEntityToTagDto(Tag tag);

    @IterableMapping(elementTargetType = TagDto.class, qualifiedByName = "fromEntityToTagDto")
    @Named("fromEntitiesToTagDtoList")
    List<TagDto> fromEntitiesToTagDtoList(List<Tag> tags);
}

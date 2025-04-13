package com.mobile.api.mapper;

import com.mobile.api.dto.file.FileDto;
import com.mobile.api.model.entity.File;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {})
public interface FileMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fileName", target = "fileName")
    @Mapping(source = "fileUrl", target = "fileUrl")
    @Mapping(source = "fileType", target = "fileType")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToFileDto")
    FileDto fromEntityToFileDto(File file);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "fileUrl", target = "fileUrl")
    @Mapping(source = "fileType", target = "fileType")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToSimpleFileDto")
    FileDto fromEntityToSimpleFileDto(File file);

    @IterableMapping(elementTargetType = FileDto.class, qualifiedByName = "fromEntityToFileDto")
    List<FileDto> fromEntitiesToFileDto(List<File> files);
}

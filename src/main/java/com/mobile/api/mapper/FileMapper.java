package com.mobile.api.mapper;

import com.mobile.api.dto.file.FileDto;
import com.mobile.api.model.entity.File;
import org.mapstruct.*;

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
}

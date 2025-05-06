package com.clubflow.dto.mapper;

import com.clubflow.dto.request.MaterialRequestDto;
import com.clubflow.model.MaterialRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {LocalDate.class, DateTimeFormatter.class})
public interface MaterialRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestedBy", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "requestedDate", expression = "java(LocalDate.parse(materialRequestDto.getRequestedDate()))")
    MaterialRequest toEntity(MaterialRequestDto materialRequestDto);

    @Mapping(target = "requestedDate", expression = "java(materialRequest.getRequestedDate().toString())")
    MaterialRequestDto toDto(MaterialRequest materialRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestedBy", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "requestedDate", expression = "java(LocalDate.parse(materialRequestDto.getRequestedDate()))")
    void updateEntity(MaterialRequestDto materialRequestDto, @MappingTarget MaterialRequest materialRequest);
}
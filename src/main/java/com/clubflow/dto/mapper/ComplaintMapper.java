package com.clubflow.dto.mapper;

import com.clubflow.dto.request.ComplaintDto;
import com.clubflow.model.Complaint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {LocalDate.class, DateTimeFormatter.class})
public interface ComplaintMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submittedBy", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "response", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "date", expression = "java(LocalDate.parse(complaintDto.getDate()))")
    Complaint toEntity(ComplaintDto complaintDto);

    @Mapping(target = "date", expression = "java(complaint.getDate().toString())")
    ComplaintDto toDto(Complaint complaint);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submittedBy", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "response", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "date", expression = "java(LocalDate.parse(complaintDto.getDate()))")
    void updateEntity(ComplaintDto complaintDto, @MappingTarget Complaint complaint);
}
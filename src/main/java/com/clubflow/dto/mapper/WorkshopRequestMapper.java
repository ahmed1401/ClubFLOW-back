package com.clubflow.dto.mapper;


import com.clubflow.dto.request.WorkshopRequestDto;
import com.clubflow.model.WorkshopRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkshopRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    WorkshopRequest toEntity(WorkshopRequestDto workshopRequestDto);

    @Mapping(target = "memberId", source = "member.id")
    WorkshopRequestDto toDto(WorkshopRequest workshopRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(WorkshopRequestDto workshopRequestDto, @MappingTarget WorkshopRequest workshopRequest);
}
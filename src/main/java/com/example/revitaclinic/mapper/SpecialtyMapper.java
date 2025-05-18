package com.example.revitaclinic.mapper;

import com.example.revitaclinic.dto.Specialty.CreateSpecialtyDto;
import com.example.revitaclinic.dto.Specialty.SpecialtyDto;
import com.example.revitaclinic.dto.Specialty.UpdateSpecialtyDto;
import com.example.revitaclinic.model.Specialty;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SpecialtyMapper {
    SpecialtyDto toDto(Specialty entity);
    Specialty toEntity(CreateSpecialtyDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UpdateSpecialtyDto dto, @MappingTarget Specialty entity);
}

package com.example.revitaclinic.mapper;

import com.example.revitaclinic.dto.*;
import com.example.revitaclinic.model.Medication;
import org.mapstruct.*;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicationMapper {

    @Mapping(target = "diagnoses", expression = "java(mapDiagnoses(entity.getDiagnoses()))")
    MedicationDto toDto(Medication entity);

    Medication toEntity(CreateMedicationDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UpdateMedicationDto dto, @MappingTarget Medication entity);

    // helper map Diagnosis → DiagnosisSummaryDto
    default Set<DiagnosisSummaryDto> mapDiagnoses(Set<com.example.revitaclinic.model.Diagnosis> diags) {
        if (diags == null) return null;
        return diags.stream()
                .map(this::diagnosisSummary)
                .collect(Collectors.toSet());
    }

    default DiagnosisSummaryDto diagnosisSummary(com.example.revitaclinic.model.Diagnosis d) {
        return new DiagnosisSummaryDto(d.getId(), d.getName());
    }
}
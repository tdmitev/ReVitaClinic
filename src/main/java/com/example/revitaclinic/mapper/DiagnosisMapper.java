package com.example.revitaclinic.mapper;

import com.example.revitaclinic.dto.Diagnosis.CreateDiagnosisDto;
import com.example.revitaclinic.dto.Diagnosis.DiagnosisDto;
import com.example.revitaclinic.dto.Diagnosis.UpdateDiagnosisDto;
import com.example.revitaclinic.dto.Medication.MedicationSummaryDto;
import com.example.revitaclinic.model.Diagnosis;
import org.mapstruct.*;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiagnosisMapper {

    @Mapping(target = "medications", expression = "java(mapMedications(entity.getMedications()))")
    DiagnosisDto toDto(Diagnosis entity);

    Diagnosis toEntity(CreateDiagnosisDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UpdateDiagnosisDto dto, @MappingTarget Diagnosis entity);

    // helper map Medication → MedicationSummaryDto
    default Set<MedicationSummaryDto> mapMedications(Set<com.example.revitaclinic.model.Medication> meds) {
        if (meds == null) return null;
        return meds.stream()
                .map(this::medicationSummary)
                .collect(Collectors.toSet());
    }

    default MedicationSummaryDto medicationSummary(com.example.revitaclinic.model.Medication m) {
        return new MedicationSummaryDto(m.getId(), m.getName());
    }
}
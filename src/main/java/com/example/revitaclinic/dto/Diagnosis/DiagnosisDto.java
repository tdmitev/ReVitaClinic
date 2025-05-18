package com.example.revitaclinic.dto.Diagnosis;

import com.example.revitaclinic.dto.Medication.MedicationSummaryDto;

import java.util.Set;

public record DiagnosisDto(
        Integer id,
        String name,
        String description,
        Set<MedicationSummaryDto> medications  // вече summary DTO
) {}

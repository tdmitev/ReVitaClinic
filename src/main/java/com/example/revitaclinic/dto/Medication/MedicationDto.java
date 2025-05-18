package com.example.revitaclinic.dto.Medication;

import com.example.revitaclinic.dto.Diagnosis.DiagnosisSummaryDto;

import java.util.Set;

public record MedicationDto(
        Integer id,
        String name,
        String description,
        Set<DiagnosisSummaryDto> diagnoses
) {}
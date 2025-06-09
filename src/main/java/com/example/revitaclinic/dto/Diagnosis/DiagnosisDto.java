package com.example.revitaclinic.dto.Diagnosis;

import com.example.revitaclinic.dto.Medication.MedicationSummaryDto;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record DiagnosisDto(
        @NotNull(message = "ID is required")
        Integer id,
        @NotNull(message = "Name is required")
        String name,
        @NotNull(message = "Description is required")
        String description,
        @NotNull(message = "ICD code is required")
        Set<MedicationSummaryDto> medications
) {}

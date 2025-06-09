package com.example.revitaclinic.dto.Diagnosis;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateDiagnosisDto(
        @NotNull(message = "Name is required")
        String name,
        @NotNull(message = "Description is required")
        String description,
        @NotNull(message = "ICD code is required")
        Set<Integer> medicationIds
) {}

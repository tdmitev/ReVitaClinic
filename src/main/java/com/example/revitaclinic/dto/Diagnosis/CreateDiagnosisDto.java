package com.example.revitaclinic.dto.Diagnosis;

import java.util.Set;

public record CreateDiagnosisDto(
        String name,
        String description,
        Set<Integer> medicationIds
) {}
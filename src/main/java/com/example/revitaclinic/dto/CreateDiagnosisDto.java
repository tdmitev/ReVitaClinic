package com.example.revitaclinic.dto;

import java.util.Set;

public record CreateDiagnosisDto(
        String name,
        String description,
        Set<Integer> medicationIds
) {}
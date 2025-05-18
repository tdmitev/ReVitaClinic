package com.example.revitaclinic.dto;

import java.util.Set;

public record UpdateDiagnosisDto(
        String name,
        String description,
        Set<Integer> medicationIds
) {}

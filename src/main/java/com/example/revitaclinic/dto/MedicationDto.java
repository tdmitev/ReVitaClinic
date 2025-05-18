package com.example.revitaclinic.dto;

import java.util.Set;

public record MedicationDto(
        Integer id,
        String name,
        String description,
        Set<DiagnosisSummaryDto> diagnoses
) {}
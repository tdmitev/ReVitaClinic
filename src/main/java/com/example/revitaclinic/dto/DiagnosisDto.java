package com.example.revitaclinic.dto;

import java.util.Set;

public record DiagnosisDto(
        Integer id,
        String name,
        String description,
        Set<MedicationSummaryDto> medications  // вече summary DTO
) {}

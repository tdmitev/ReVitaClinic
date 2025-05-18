package com.example.revitaclinic.dto;

import java.util.Set;

public record UpdateMedicationDto(
        String name,
        String description,
        Set<Integer> diagnosisIds
) {}

package com.example.revitaclinic.dto;

import java.util.Set;

public record CreateMedicationDto(
        String name,
        String description,
        Set<Integer> diagnosisIds
) {}

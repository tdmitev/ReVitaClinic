package com.example.revitaclinic.dto.Medication;

import java.util.Set;

public record UpdateMedicationDto(
        String name,
        String description,
        Set<Integer> diagnosisIds
) {}

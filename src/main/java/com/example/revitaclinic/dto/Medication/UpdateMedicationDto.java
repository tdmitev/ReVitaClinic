package com.example.revitaclinic.dto.Medication;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateMedicationDto(
        @Size(max = 255) String name,
        @Size(max = 10000) String description,
        Set<@NotNull Integer> diagnosisIds
) {}

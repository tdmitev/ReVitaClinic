package com.example.revitaclinic.dto.Medication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateMedicationDto(
        @NotBlank
        String name,
        String description,
        Set<@NotNull Integer> diagnosisIds
) {}

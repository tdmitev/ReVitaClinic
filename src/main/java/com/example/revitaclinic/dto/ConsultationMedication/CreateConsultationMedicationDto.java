package com.example.revitaclinic.dto.ConsultationMedication;

import jakarta.validation.constraints.NotNull;

public record CreateConsultationMedicationDto(
        @NotNull(message = "Consultation ID is required")
        Integer medicationId,
        @NotNull(message = "Medication ID is required")
        String dosage,
        @NotNull(message = "Frequency is required")
        String frequency,
        @NotNull(message = "Duration is required")
        String duration
) {}

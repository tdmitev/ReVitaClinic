package com.example.revitaclinic.dto.ConsultationMedication;

import com.example.revitaclinic.dto.Medication.MedicationDto;
import jakarta.validation.constraints.NotNull;

public record ConsultationMedicationDto(
        @NotNull(message = "Consultation ID is required")
        MedicationDto medication,
        @NotNull(message = "Dosage is required")
        String dosage,
        @NotNull(message = "Frequency is required")
        String frequency,
        @NotNull(message = "Duration is required")
        String duration
) {}

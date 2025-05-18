package com.example.revitaclinic.dto.ConsultationMedication;

import com.example.revitaclinic.dto.Medication.MedicationDto;

public record ConsultationMedicationDto(
        MedicationDto medication,
        String dosage,
        String frequency,
        String duration
) {}

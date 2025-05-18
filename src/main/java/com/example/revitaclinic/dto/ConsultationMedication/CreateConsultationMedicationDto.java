package com.example.revitaclinic.dto.ConsultationMedication;

public record CreateConsultationMedicationDto(
        Integer medicationId,
        String dosage,
        String frequency,
        String duration
) {}

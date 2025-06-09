package com.example.revitaclinic.dto.Consultation;

import com.example.revitaclinic.dto.ConsultationMedication.CreateConsultationMedicationDto;
import com.example.revitaclinic.dto.SickLeave.CreateSickLeaveDto;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UpdateConsultationDto(
        @NotNull(message = "Date cannot be null")
        LocalDateTime date,
        @NotNull(message = "Doctor ID cannot be null")
        UUID doctorId,
        @NotNull(message = "Patient ID cannot be null")
        UUID patientId,
        @NotNull(message = "Diagnosis ID cannot be null")
        Integer diagnosisId,
        String notes,
        List<CreateConsultationMedicationDto> medicationEntries,
        CreateSickLeaveDto sickLeave
) {}
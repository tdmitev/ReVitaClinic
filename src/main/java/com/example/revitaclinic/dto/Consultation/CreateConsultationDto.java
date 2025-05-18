package com.example.revitaclinic.dto.Consultation;

import com.example.revitaclinic.dto.ConsultationMedication.CreateConsultationMedicationDto;
import com.example.revitaclinic.dto.SickLeave.CreateSickLeaveDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateConsultationDto(
        LocalDateTime date,
        UUID doctorId,
        UUID patientId,
        Integer diagnosisId,
        String notes,
        List<CreateConsultationMedicationDto> medicationEntries,
        CreateSickLeaveDto sickLeave
) {}
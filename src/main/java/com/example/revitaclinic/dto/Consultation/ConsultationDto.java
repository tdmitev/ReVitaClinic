package com.example.revitaclinic.dto.Consultation;

import com.example.revitaclinic.dto.ConsultationMedication.ConsultationMedicationDto;
import com.example.revitaclinic.dto.Diagnosis.DiagnosisDto;
import com.example.revitaclinic.dto.Doctor.DoctorDto;
import com.example.revitaclinic.dto.Patient.PatientDto;
import com.example.revitaclinic.dto.SickLeave.SickLeaveDto;

import java.time.LocalDateTime;
import java.util.List;

public record ConsultationDto(
        Integer id,
        LocalDateTime date,
        DoctorDto doctor,
        PatientDto patient,
        DiagnosisDto diagnosis,
        String notes,
        List<ConsultationMedicationDto> medicationEntries,
        SickLeaveDto sickLeave
) {}

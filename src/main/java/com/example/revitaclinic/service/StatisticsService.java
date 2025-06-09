package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Diagnosis.DiagnosisDto;
import com.example.revitaclinic.dto.Doctor.DoctorDto;
import com.example.revitaclinic.dto.Patient.PatientDto;
import com.example.revitaclinic.dto.Stats.DoctorCountDto;

import java.util.List;
import java.util.UUID;

public interface StatisticsService {
    List<PatientDto> patientsByDiagnosis(Integer diagnosisId);
    DiagnosisDto mostCommonDiagnosis();
    List<DoctorCountDto> patientCountPerDoctor();
    List<DoctorCountDto> consultationCountPerDoctor();
    Integer monthWithMostSickLeaves();
    DoctorDto doctorWithMostSickLeaves();
}

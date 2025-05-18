package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Patient.CreatePatientDto;
import com.example.revitaclinic.dto.Patient.PatientDto;
import com.example.revitaclinic.dto.Patient.UpdatePatientDto;
import com.example.revitaclinic.model.Patient;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    PatientDto create(CreatePatientDto dto);
    PatientDto findById(UUID id);
    PatientDto update(UUID id, UpdatePatientDto dto);
    List<PatientDto> findAll();
    void delete(UUID id);
    Patient getEntity(UUID keycloakUserId);
}
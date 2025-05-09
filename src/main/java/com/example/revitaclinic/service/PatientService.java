package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.CreatePatientDto;
import com.example.revitaclinic.dto.PatientDto;
import com.example.revitaclinic.dto.UpdatePatientDto;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    PatientDto create(CreatePatientDto dto);
    PatientDto findById(UUID id);
    PatientDto update(UUID id, UpdatePatientDto dto);
    List<PatientDto> findAll();
    void delete(UUID id);
}
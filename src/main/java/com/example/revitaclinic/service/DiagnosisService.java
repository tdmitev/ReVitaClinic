package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Diagnosis.CreateDiagnosisDto;
import com.example.revitaclinic.dto.Diagnosis.DiagnosisDto;
import com.example.revitaclinic.dto.Diagnosis.UpdateDiagnosisDto;
import com.example.revitaclinic.model.Diagnosis;
import java.util.List;

public interface DiagnosisService {
    DiagnosisDto create(CreateDiagnosisDto dto);
    DiagnosisDto findById(Integer id);
    List<DiagnosisDto> findAll();
    DiagnosisDto update(Integer id, UpdateDiagnosisDto dto);
    void delete(Integer id);
    Diagnosis getEntity(Integer id);
}

package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Medication.CreateMedicationDto;
import com.example.revitaclinic.dto.Medication.MedicationDto;
import com.example.revitaclinic.dto.Medication.UpdateMedicationDto;
import com.example.revitaclinic.model.Medication;
import java.util.List;

public interface MedicationService {
    MedicationDto create(CreateMedicationDto dto);
    MedicationDto findById(Integer id);
    List<MedicationDto> findAll();
    MedicationDto update(Integer id, UpdateMedicationDto dto);
    void delete(Integer id);
    Medication getEntity(Integer id);
}

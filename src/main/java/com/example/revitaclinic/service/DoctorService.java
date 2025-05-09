package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.CreateDoctorDto;
import com.example.revitaclinic.dto.DoctorDto;
import com.example.revitaclinic.dto.UpdateDoctorDto;
import com.example.revitaclinic.model.Doctor;

import java.util.List;
import java.util.UUID;

public interface DoctorService {
    DoctorDto create(CreateDoctorDto dto);
    DoctorDto findById(UUID id);
    DoctorDto update(UUID id, UpdateDoctorDto dto);
    Doctor getEntity(UUID id);
    List<DoctorDto> findAll();
    void delete(UUID id);
}
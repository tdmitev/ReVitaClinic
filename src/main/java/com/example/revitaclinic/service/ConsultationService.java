package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Consultation.ConsultationDto;
import com.example.revitaclinic.dto.Consultation.CreateConsultationDto;
import com.example.revitaclinic.dto.Consultation.UpdateConsultationDto;
import com.example.revitaclinic.model.Consultation;

import java.util.List;
import java.util.UUID;

public interface ConsultationService {
    ConsultationDto create(CreateConsultationDto dto);
    ConsultationDto findById(Integer id);
    List<ConsultationDto> findAll();
    List<ConsultationDto> findByPatient(UUID patientId);
    List<ConsultationDto> findByDoctor(UUID doctorId);
    List<ConsultationDto> findByPeriod(String start, String end);
    List<ConsultationDto> findByDoctorAndPeriod(UUID doctorId, String start, String end);
    ConsultationDto update(Integer id, UpdateConsultationDto dto);
    void delete(Integer id);
    Consultation getEntity(Integer id);
}
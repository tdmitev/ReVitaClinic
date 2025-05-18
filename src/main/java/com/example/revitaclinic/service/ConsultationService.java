package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Consultation.ConsultationDto;
import com.example.revitaclinic.dto.Consultation.CreateConsultationDto;
import com.example.revitaclinic.dto.Consultation.UpdateConsultationDto;
import com.example.revitaclinic.model.Consultation;

import java.util.List;

public interface ConsultationService {
    ConsultationDto create(CreateConsultationDto dto);
    ConsultationDto findById(Integer id);
    List<ConsultationDto> findAll();
    ConsultationDto update(Integer id, UpdateConsultationDto dto);
    void delete(Integer id);
    Consultation getEntity(Integer id);
}
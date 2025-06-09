package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.SickLeave.CreateSickLeaveDto;
import com.example.revitaclinic.dto.SickLeave.SickLeaveDto;

public interface SickLeaveService {
    SickLeaveDto create(Integer consultationId, CreateSickLeaveDto dto);
    SickLeaveDto findById(Integer id);
    SickLeaveDto update(Integer id, CreateSickLeaveDto dto);
    void delete(Integer id);
}

package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.*;
import com.example.revitaclinic.model.Specialty;

import java.util.List;

public interface SpecialtyService {
    SpecialtyDto create(CreateSpecialtyDto dto);
    SpecialtyDto findById(Integer id);
    List<SpecialtyDto> findAll();
    SpecialtyDto update(Integer id, UpdateSpecialtyDto dto);
    void delete(Integer id);
    Specialty getEntity(Integer id);
}
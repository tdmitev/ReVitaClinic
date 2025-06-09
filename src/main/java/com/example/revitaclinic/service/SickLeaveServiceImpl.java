package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.SickLeave.CreateSickLeaveDto;
import com.example.revitaclinic.dto.SickLeave.SickLeaveDto;
import com.example.revitaclinic.exception.ResourceNotFoundException;
import com.example.revitaclinic.model.Consultation;
import com.example.revitaclinic.model.SickLeave;
import com.example.revitaclinic.repository.SickLeaveRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.revitaclinic.config.SecurityUtils;

@Service
@Transactional
public class SickLeaveServiceImpl implements SickLeaveService {
    private final SickLeaveRepository repo;
    private final ConsultationService consultationService;

    public SickLeaveServiceImpl(SickLeaveRepository repo, ConsultationService consultationService) {
        this.repo = repo;
        this.consultationService = consultationService;
    }

    @Override
    public SickLeaveDto create(Integer consultationId, CreateSickLeaveDto dto) {
        Consultation c = consultationService.getEntity(consultationId);
        if (!c.getDoctor().getKeycloakUserId()
                .equals(SecurityUtils.getCurrentUserId())) {
            throw new IllegalArgumentException("Doctor can create only own sick leaves");
        }
        SickLeave sl = new SickLeave();
        sl.setConsultation(c);
        sl.setStartDate(dto.startDate());
        sl.setNumberOfDays(dto.numberOfDays());
        repo.save(sl);
        return new SickLeaveDto(sl.getStartDate(), sl.getNumberOfDays());
    }

    @Override
    public SickLeaveDto findById(Integer id) {
        SickLeave sl = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SickLeave not found: " + id));
        return new SickLeaveDto(sl.getStartDate(), sl.getNumberOfDays());
    }

    @Override
    public SickLeaveDto update(Integer id, CreateSickLeaveDto dto) {
        SickLeave sl = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SickLeave not found: " + id));
        Consultation c = sl.getConsultation();
        if (!c.getDoctor().getKeycloakUserId()
                .equals(SecurityUtils.getCurrentUserId())) {
            throw new IllegalArgumentException("Doctor can update only own sick leaves");
        }
        sl.setStartDate(dto.startDate());
        sl.setNumberOfDays(dto.numberOfDays());
        return new SickLeaveDto(sl.getStartDate(), sl.getNumberOfDays());
    }

    @Override
    public void delete(Integer id) {
        SickLeave sl = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SickLeave not found: " + id));
        Consultation c = sl.getConsultation();
        if (!c.getDoctor().getKeycloakUserId()
                .equals(SecurityUtils.getCurrentUserId())) {
            throw new IllegalArgumentException("Doctor can delete only own sick leaves");
        }
        repo.delete(sl);
    }
}

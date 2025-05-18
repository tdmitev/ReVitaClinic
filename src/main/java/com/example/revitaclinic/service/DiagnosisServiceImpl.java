package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Diagnosis.CreateDiagnosisDto;
import com.example.revitaclinic.dto.Diagnosis.DiagnosisDto;
import com.example.revitaclinic.dto.Diagnosis.UpdateDiagnosisDto;
import com.example.revitaclinic.exception.ResourceNotFoundException;
import com.example.revitaclinic.mapper.DiagnosisMapper;
import com.example.revitaclinic.model.Diagnosis;
import com.example.revitaclinic.model.Medication;
import com.example.revitaclinic.repository.DiagnosisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepo;
    private final DiagnosisMapper mapper;
    private final MedicationDiagnosisService  medDiagService;

    public DiagnosisServiceImpl(
            DiagnosisRepository diagnosisRepo,
            DiagnosisMapper mapper,
            MedicationDiagnosisService  medDiagService
    ) {
        this.diagnosisRepo = diagnosisRepo;
        this.mapper = mapper;
        this.medDiagService   = medDiagService;
    }


    @Override
    public DiagnosisDto create(CreateDiagnosisDto dto) {
        Diagnosis diag = mapper.toEntity(dto);
        diagnosisRepo.save(diag);
        DiagnosisDto result = mapper.toDto(diag);
        if (dto.medicationIds() != null) {
            for (Integer medId : dto.medicationIds()) {
                result = medDiagService.addMedicationToDiagnosis(diag.getId(), medId);
            }
        }

        return result;
    }

    @Override
    public DiagnosisDto update(Integer id, UpdateDiagnosisDto dto) {
        Diagnosis diag = getEntity(id);
        mapper.updateEntityFromDto(dto, diag);
        diagnosisRepo.save(diag);

        if (dto.medicationIds() != null) {
            Set<Integer> toKeep  = Set.copyOf(dto.medicationIds());
            Set<Integer> current = diag.getMedications().stream()
                    .map(Medication::getId)
                    .collect(Collectors.toSet());
            DiagnosisDto result  = null;

            for (Integer oldId : current) {
                if (!toKeep.contains(oldId)) {
                    result = medDiagService.removeMedicationFromDiagnosis(id, oldId);
                }
            }
            for (Integer newId : toKeep) {
                if (!current.contains(newId)) {
                    result = medDiagService.addMedicationToDiagnosis(id, newId);
                }
            }
            if (result != null) {
                return result;
            }
        }

        return mapper.toDto(diag);
    }


    @Override
    public DiagnosisDto findById(Integer id) {
        return mapper.toDto(getEntity(id));
    }

    @Override
    public List<DiagnosisDto> findAll() {
        return diagnosisRepo.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public void delete(Integer id) {
        if (!diagnosisRepo.existsById(id)) {
            throw new ResourceNotFoundException("Diagnosis not found: " + id);
        }
        diagnosisRepo.deleteById(id);
    }

    @Override
    public Diagnosis getEntity(Integer id) {
        return diagnosisRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found: " + id));
    }
}
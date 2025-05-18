package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Medication.CreateMedicationDto;
import com.example.revitaclinic.dto.Medication.MedicationDto;
import com.example.revitaclinic.dto.Medication.UpdateMedicationDto;
import com.example.revitaclinic.exception.ResourceNotFoundException;
import com.example.revitaclinic.mapper.MedicationMapper;
import com.example.revitaclinic.model.Diagnosis;
import com.example.revitaclinic.model.Medication;
import com.example.revitaclinic.repository.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepo;
    private final MedicationMapper mapper;
    private final MedicationDiagnosisService  medDiagService;

    public MedicationServiceImpl(
            MedicationRepository medicationRepo,
            MedicationMapper mapper,
            MedicationDiagnosisService  medDiagService
    ) {
        this.medicationRepo = medicationRepo;
        this.mapper = mapper;
        this.medDiagService   = medDiagService;
    }


    @Override
    public MedicationDto create(CreateMedicationDto dto) {
        Medication med = mapper.toEntity(dto);
        medicationRepo.save(med);

        MedicationDto result = mapper.toDto(med);
        if (dto.diagnosisIds() != null) {
            for (Integer diagId : dto.diagnosisIds()) {
                result = medDiagService.addDiagnosisToMedication(med.getId(), diagId);
            }
        }
        return result;
    }

    @Override
    public MedicationDto update(Integer id, UpdateMedicationDto dto) {
        Medication med = getEntity(id);
        mapper.updateEntityFromDto(dto, med);
        medicationRepo.save(med);

        if (dto.diagnosisIds() != null) {
            Set<Integer> toKeep   = Set.copyOf(dto.diagnosisIds());
            Set<Integer> current  = med.getDiagnoses().stream()
                    .map(Diagnosis::getId)
                    .collect(Collectors.toSet());
            MedicationDto result  = null;

            for (Integer oldId : current) {
                if (!toKeep.contains(oldId)) {
                    result = medDiagService.removeDiagnosisFromMedication(id, oldId);
                }
            }
            for (Integer newId : toKeep) {
                if (!current.contains(newId)) {
                    result = medDiagService.addDiagnosisToMedication(id, newId);
                }
            }
            if (result != null) {
                return result;
            }
        }
        return mapper.toDto(med);
    }

    @Override
    public MedicationDto findById(Integer id) {
        return mapper.toDto(getEntity(id));
    }

    @Override
    public List<MedicationDto> findAll() {
        return medicationRepo.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    @Override
    public void delete(Integer id) {
        medDiagService.removeAllDiagnosesFromMedication(id);
        medicationRepo.deleteById(id);
    }

    @Override
    public Medication getEntity(Integer id) {
        return medicationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found: " + id));
    }
}
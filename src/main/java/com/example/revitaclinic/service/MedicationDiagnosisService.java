package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Diagnosis.DiagnosisDto;
import com.example.revitaclinic.dto.Medication.MedicationDto;
import com.example.revitaclinic.exception.ResourceNotFoundException;
import com.example.revitaclinic.mapper.DiagnosisMapper;
import com.example.revitaclinic.mapper.MedicationMapper;
import com.example.revitaclinic.model.Diagnosis;
import com.example.revitaclinic.model.Medication;
import com.example.revitaclinic.repository.DiagnosisRepository;
import com.example.revitaclinic.repository.MedicationRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MedicationDiagnosisService {

    private final MedicationRepository medicationRepo;
    private final DiagnosisRepository  diagnosisRepo;
    private final MedicationMapper     medicationMapper;
    private final DiagnosisMapper      diagnosisMapper;
    private final EntityManager em;

    public MedicationDiagnosisService(
            MedicationRepository medicationRepo,
            DiagnosisRepository  diagnosisRepo,
            MedicationMapper     medicationMapper,
            DiagnosisMapper      diagnosisMapper,
            EntityManager        em
    ) {
        this.medicationRepo   = medicationRepo;
        this.diagnosisRepo    = diagnosisRepo;
        this.medicationMapper = medicationMapper;
        this.diagnosisMapper  = diagnosisMapper;
        this.em               = em;
    }

    public void removeAllDiagnosesFromMedication(Integer medId) {
        List<Diagnosis> linked = diagnosisRepo.findAllByMedications_Id(medId);
        for (Diagnosis diag : linked) {
            if (diag.getMedications().removeIf(m -> m.getId().equals(medId))) {
                diagnosisRepo.save(diag);
            }
        }
    }

    /* ---------- MEDICATION → add/remove diagnosis ---------- */

    public MedicationDto addDiagnosisToMedication(Integer medId, Integer diagId) {
        Diagnosis diag = diagnosisRepo.findById(diagId)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found: " + diagId));
        Medication med = medicationRepo.findById(medId)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found: " + medId));

        if (diag.getMedications().add(med)) {
            diagnosisRepo.save(diag);
        }

        em.flush();
        em.clear();

        Medication fresh = medicationRepo.findById(medId).get();
        return medicationMapper.toDto(fresh);
    }

    public MedicationDto removeDiagnosisFromMedication(Integer medId, Integer diagId) {
        Diagnosis diag = diagnosisRepo.findById(diagId)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found: " + diagId));
        Medication med = medicationRepo.findById(medId)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found: " + medId));

        if (diag.getMedications().remove(med)) {
            diagnosisRepo.save(diag);
        }

        em.flush();
        em.clear();

        Medication fresh = medicationRepo.findById(medId).get();
        return medicationMapper.toDto(fresh);
    }

    /* ---------- DIAGNOSIS → add/remove medication ---------- */

    public DiagnosisDto addMedicationToDiagnosis(Integer diagId, Integer medId) {
        Diagnosis diag = diagnosisRepo.findById(diagId)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found: " + diagId));
        Medication med = medicationRepo.findById(medId)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found: " + medId));

        if (diag.getMedications().add(med)) {
            diagnosisRepo.save(diag);
        }

        em.flush();
        em.clear();

        Diagnosis fresh = diagnosisRepo.findById(diagId).get();
        return diagnosisMapper.toDto(fresh);
    }

    public DiagnosisDto removeMedicationFromDiagnosis(Integer diagId, Integer medId) {
        Diagnosis diag = diagnosisRepo.findById(diagId)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found: " + diagId));
        Medication med = medicationRepo.findById(medId)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found: " + medId));

        if (diag.getMedications().remove(med)) {
            diagnosisRepo.save(diag);
        }

        em.flush();
        em.clear();

        Diagnosis fresh = diagnosisRepo.findById(diagId).get();
        return diagnosisMapper.toDto(fresh);
    }
}
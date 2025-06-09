package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Consultation.ConsultationDto;
import com.example.revitaclinic.dto.Consultation.CreateConsultationDto;
import com.example.revitaclinic.dto.Consultation.UpdateConsultationDto;
import com.example.revitaclinic.dto.ConsultationMedication.CreateConsultationMedicationDto;
import com.example.revitaclinic.exception.ResourceNotFoundException;
import com.example.revitaclinic.mapper.ConsultationMapper;
import com.example.revitaclinic.model.*;
import com.example.revitaclinic.repository.ConsultationRepository;
import com.example.revitaclinic.config.SecurityUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository repo;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final DiagnosisService diagnosisService;
    private final MedicationService medicationService;
    private final ConsultationMapper mapper;
    private final EntityManager em;

    public ConsultationServiceImpl(
            ConsultationRepository repo,
            DoctorService doctorService,
            PatientService patientService,
            DiagnosisService diagnosisService,
            MedicationService medicationService,
            ConsultationMapper mapper,
            EntityManager em
    ) {
        this.repo = repo;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.diagnosisService = diagnosisService;
        this.medicationService = medicationService;
        this.mapper = mapper;
        this.em = em;
    }

    @Override
    public ConsultationDto create(CreateConsultationDto dto) {
        Consultation c = new Consultation();
        c.setDate(dto.date());

        Doctor doc = doctorService.getEntity(dto.doctorId());
        c.setDoctor(doc);

        Patient pat = patientService.getEntity(dto.patientId());
        c.setPatient(pat);

        c.setDiagnosis(diagnosisService.getEntity(dto.diagnosisId()));
        c.setNotes(dto.notes());

        if (dto.medicationEntries() != null) {
            for (CreateConsultationMedicationDto m : dto.medicationEntries()) {
                ConsultationMedication cm = new ConsultationMedication();
                cm.setId(new ConsultationMedicationId());
                cm.getId().setConsultationId(null);
                cm.getId().setMedicationId(m.medicationId());
                cm.setConsultation(c);
                cm.setMedication(medicationService.getEntity(m.medicationId()));
                cm.setDosage(m.dosage());
                cm.setFrequency(m.frequency());
                cm.setDuration(m.duration());
                c.getMedicationEntries().add(cm);
            }
        }

        if (dto.sickLeave() != null) {
            SickLeave sl = new SickLeave();
            sl.setConsultation(c);
            sl.setStartDate(dto.sickLeave().startDate());
            sl.setNumberOfDays(dto.sickLeave().numberOfDays());
            c.setSickLeave(sl);
        }

        Consultation saved = repo.save(c);
        em.flush();
        em.clear();

        return mapper.toDto(repo.findById(saved.getId()).orElseThrow());
    }

    @Override
    public ConsultationDto findById(Integer id) {
        return mapper.toDto(getEntity(id));
    }

    @Override
    public List<ConsultationDto> findAll() {
        List<ConsultationDto> list = new ArrayList<>();
        for (Consultation c : repo.findAll()) {
            list.add(mapper.toDto(c));
        }
        return list;
    }

    @Override
    public List<ConsultationDto> findByPatient(UUID patientId) {
        return repo.findByPatient_KeycloakUserId(patientId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<ConsultationDto> findByDoctor(UUID doctorId) {
        return repo.findByDoctor_KeycloakUserId(doctorId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<ConsultationDto> findByPeriod(String start, String end) {
        LocalDateTime s = LocalDateTime.parse(start);
        LocalDateTime e = LocalDateTime.parse(end);
        return repo.findByDateBetween(s, e)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<ConsultationDto> findByDoctorAndPeriod(UUID doctorId, String start, String end) {
        LocalDateTime s = LocalDateTime.parse(start);
        LocalDateTime e = LocalDateTime.parse(end);
        return repo.findByDoctor_KeycloakUserIdAndDateBetween(doctorId, s, e)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ConsultationDto update(Integer id, UpdateConsultationDto dto) {
        Consultation c = getEntity(id);
        UUID currentDoctor = SecurityUtils.getCurrentUserId();
        if (!c.getDoctor().getKeycloakUserId().equals(currentDoctor)) {
            throw new IllegalArgumentException("Doctor can update only own consultations");
        }
        c.setDate(dto.date());

        Doctor doc = doctorService.getEntity(dto.doctorId());
        c.setDoctor(doc);

        Patient pat = patientService.getEntity(dto.patientId());
        c.setPatient(pat);

        c.setDiagnosis(diagnosisService.getEntity(dto.diagnosisId()));
        c.setNotes(dto.notes());

        c.getMedicationEntries().clear();
        if (dto.medicationEntries() != null) {
            for (CreateConsultationMedicationDto m : dto.medicationEntries()) {
                ConsultationMedication cm = new ConsultationMedication();
                cm.setId(new ConsultationMedicationId(id, m.medicationId()));
                cm.setConsultation(c);
                cm.setMedication(medicationService.getEntity(m.medicationId()));
                cm.setDosage(m.dosage());
                cm.setFrequency(m.frequency());
                cm.setDuration(m.duration());
                c.getMedicationEntries().add(cm);
            }
        }

        if (dto.sickLeave() != null) {
            SickLeave sl = c.getSickLeave();
            if (sl == null) {
                sl = new SickLeave();
                sl.setConsultation(c);
                c.setSickLeave(sl);
            }
            sl.setStartDate(dto.sickLeave().startDate());
            sl.setNumberOfDays(dto.sickLeave().numberOfDays());
        } else {
            c.setSickLeave(null);
        }

        Consultation updated = repo.save(c);
        em.flush();
        em.clear();
        return mapper.toDto(repo.findById(updated.getId()).orElseThrow());
    }

    @Override
    public void delete(Integer id) {
        Consultation c = getEntity(id);
        UUID currentDoctor = SecurityUtils.getCurrentUserId();
        if (!c.getDoctor().getKeycloakUserId().equals(currentDoctor)) {
            throw new IllegalArgumentException("Doctor can delete only own consultations");
        }
        repo.delete(c);
    }

    @Override
    public Consultation getEntity(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found: " + id));
    }
}
package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Patient.CreatePatientDto;
import com.example.revitaclinic.dto.Patient.PatientDto;
import com.example.revitaclinic.dto.Patient.UpdatePatientDto;
import com.example.revitaclinic.exception.ResourceNotFoundException;
import com.example.revitaclinic.mapper.PatientMapper;
import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.model.Doctor;
import com.example.revitaclinic.model.Patient;
import com.example.revitaclinic.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepo;
    private final PatientMapper mapper;
    private final KeycloakService kc;
    private final AppUserService userService;
    private final DoctorService doctorService;

    public PatientServiceImpl(PatientRepository patientRepo,
                              PatientMapper mapper,
                              KeycloakService kc,
                              AppUserService userService,
                              DoctorService doctorService) {
        this.patientRepo    = patientRepo;
        this.mapper         = mapper;
        this.kc             = kc;
        this.userService    = userService;
        this.doctorService  = doctorService;
    }

    @Override
    public Patient getEntity(UUID keycloakUserId) {
        return patientRepo.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found: " + keycloakUserId));
    }

    @Override
    public PatientDto create(CreatePatientDto dto) {
        if (dto.healthInsuranceLastPayment() == null ||
                dto.healthInsuranceLastPayment().isBefore(java.time.LocalDate.now().minusMonths(6))) {
            throw new IllegalArgumentException("Health insurance not paid in last 6 months");
        }
        kc.assignRealmRole(dto.keycloakUserId(), "PATIENT");
        AppUser user = userService.upsertUser(dto.keycloakUserId(), dto.phone());
        Patient p = mapper.toEntity(dto);
        p.setUser(user);
        Doctor doc = doctorService.getEntity(dto.personalDoctorId());
        p.setPersonalDoctor(doc);
        doc.setPersonal(true);
        Patient saved = patientRepo.save(p);
        return mapper.toDto(saved);
    }

    @Override
    public List<PatientDto> findAll() {
        return patientRepo.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public PatientDto findById(UUID keycloakUserId) {
        return mapper.toDto(getEntity(keycloakUserId));
    }

    @Override
    @Transactional
    public PatientDto update(UUID keycloakUserId, UpdatePatientDto dto) {
        Patient p = getEntity(keycloakUserId);
        if (dto.phone() != null) {
            userService.upsertUser(keycloakUserId, dto.phone());
        }
        if (dto.personalDoctorId() != null) {
            Doctor old = p.getPersonalDoctor();
            if (old != null) {
                old.setPersonal(false);
            }
            Doctor neu = doctorService.getEntity(dto.personalDoctorId());
            neu.setPersonal(true);
            p.setPersonalDoctor(neu);
        }
        if (dto.healthInsuranceLastPayment() != null &&
                dto.healthInsuranceLastPayment().isBefore(java.time.LocalDate.now().minusMonths(6))) {
            throw new IllegalArgumentException("Health insurance not paid in last 6 months");
        }
        mapper.updateEntityFromDto(dto, p);

        return mapper.toDto(p);
    }

    @Override
    @Transactional
    public void delete(UUID keycloakUserId) {
        Patient p = getEntity(keycloakUserId);
        Doctor doc = p.getPersonalDoctor();
        if (doc != null) {
            doc.setPersonal(false);
        }
        patientRepo.delete(p);
    }
}
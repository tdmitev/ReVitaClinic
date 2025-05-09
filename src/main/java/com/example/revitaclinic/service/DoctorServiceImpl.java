package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.*;
import com.example.revitaclinic.exception.ResourceNotFoundException;
import com.example.revitaclinic.mapper.DoctorMapper;
import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.model.Doctor;
import com.example.revitaclinic.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepo;
    private final DoctorMapper mapper;
    private final KeycloakService kc;
    private final AppUserService userService;

    public DoctorServiceImpl(DoctorRepository doctorRepo,
                             DoctorMapper mapper,
                             KeycloakService kc,
                             AppUserService userService) {
        this.doctorRepo  = doctorRepo;
        this.mapper      = mapper;
        this.kc          = kc;
        this.userService = userService;
    }

    @Override
    public DoctorDto create(CreateDoctorDto dto) {
        kc.assignRealmRole(dto.keycloakUserId(), "DOCTOR");
        AppUser user = userService.upsertUser(dto.keycloakUserId(), dto.phone());
        Doctor doc = mapper.toEntity(dto);
        doc.setUser(user);
        Doctor saved = doctorRepo.save(doc);
        return mapper.toDto(saved);
    }

    @Override
    public List<DoctorDto> findAll() {
        return doctorRepo.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public DoctorDto findById(UUID keycloakUserId) {
        Doctor doc = doctorRepo.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found: " + keycloakUserId));
        return mapper.toDto(doc);
    }

    @Override
    @Transactional
    public DoctorDto update(UUID keycloakUserId, UpdateDoctorDto dto) {
        Doctor doc = doctorRepo.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found: " + keycloakUserId));
        if (dto.phone() != null) {
            AppUser user = userService.upsertUser(keycloakUserId, dto.phone());
            doc.setUser(user);
        }
        mapper.updateEntityFromDto(dto, doc);
        return mapper.toDto(doc);
    }

    @Override
    @Transactional
    public void delete(UUID keycloakUserId) {
        if (!doctorRepo.existsByKeycloakUserId(keycloakUserId)) {
            throw new ResourceNotFoundException("Doctor not found: " + keycloakUserId);
        }
        Doctor doc = doctorRepo.findByKeycloakUserId(keycloakUserId).get();
        doctorRepo.delete(doc);
    }

    @Override
    public Doctor getEntity(UUID keycloakUserId) {
        return doctorRepo.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found: " + keycloakUserId));
    }
}
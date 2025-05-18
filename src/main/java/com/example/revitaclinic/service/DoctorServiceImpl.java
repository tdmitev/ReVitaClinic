package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Doctor.CreateDoctorDto;
import com.example.revitaclinic.dto.Doctor.DoctorDto;
import com.example.revitaclinic.dto.Doctor.UpdateDoctorDto;
import com.example.revitaclinic.exception.ResourceNotFoundException;
import com.example.revitaclinic.mapper.DoctorMapper;
import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.model.Doctor;
import com.example.revitaclinic.model.Specialty;
import com.example.revitaclinic.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepo;
    private final DoctorMapper mapper;
    private final KeycloakService kc;
    private final AppUserService userService;
    private final SpecialtyService specialtyService;    // <- ново

    public DoctorServiceImpl(DoctorRepository doctorRepo,
                             DoctorMapper mapper,
                             KeycloakService kc,
                             AppUserService userService,
                             SpecialtyService specialtyService) {
        this.doctorRepo = doctorRepo;
        this.mapper = mapper;
        this.kc = kc;
        this.userService = userService;
        this.specialtyService = specialtyService;
    }

    @Override
    public DoctorDto create(CreateDoctorDto dto) {
        kc.assignRealmRole(dto.keycloakUserId(), "DOCTOR");
        AppUser user = userService.upsertUser(dto.keycloakUserId(), dto.phone());

        Doctor doc = mapper.toEntity(dto);
        doc.setKeycloakUserId(user.getKeycloakUserId());
        doc.setUser(user);

        if (dto.specialtyIds() != null && !dto.specialtyIds().isEmpty()) {
            Set<Specialty> specs = dto.specialtyIds().stream()
                    .map(specialtyService::getEntity)
                    .collect(Collectors.toSet());
            doc.setSpecialties(specs);
        }

        doctorRepo.save(doc);
        return mapper.toDto(doc);
    }

    @Override
    public DoctorDto update(UUID id, UpdateDoctorDto dto) {
        Doctor doc = getEntity(id);

        if (dto.phone() != null) {
            AppUser user = userService.upsertUser(id, dto.phone());
            doc.setUser(user);
        }
        if (dto.specialtyIds() != null) {
            Set<Specialty> specs = dto.specialtyIds().stream()
                    .map(specialtyService::getEntity)
                    .collect(Collectors.toSet());
            doc.setSpecialties(specs);
        }

        mapper.updateEntityFromDto(dto, doc);
        return mapper.toDto(doc);
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
package com.example.revitaclinic.service;

import com.example.revitaclinic.dto.Doctor.CreateDoctorDto;
import com.example.revitaclinic.dto.Doctor.DoctorDto;
import com.example.revitaclinic.exception.ResourceNotFoundException;
import com.example.revitaclinic.mapper.DoctorMapper;
import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.model.Doctor;
import com.example.revitaclinic.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private DoctorMapper mapper;
    @Mock
    private KeycloakService keycloakService;
    @Mock
    private AppUserService userService;
    @Mock
    private SpecialtyService specialtyService;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private UUID id;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        doctor = new Doctor();
        doctor.setUniqueId("UID1");
        doctor.setPersonal(true);
        AppUser user = new AppUser();
        user.setKeycloakUserId(id);
        doctor.setUser(user);
    }

    @Test
    void getEntity_found() {
        when(doctorRepository.findByKeycloakUserId(id)).thenReturn(Optional.of(doctor));
        Doctor result = doctorService.getEntity(id);
        assertThat(result).isEqualTo(doctor);
    }

    @Test
    void delete_notFound_throws() {
        when(doctorRepository.existsByKeycloakUserId(id)).thenReturn(false);
        assertThatThrownBy(() -> doctorService.delete(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_assignsRoleAndSaves() {
        CreateDoctorDto dto = new CreateDoctorDto(id, "0888", "UID1", true, Set.of());
        when(mapper.toEntity(dto)).thenReturn(doctor);
        when(userService.upsertUser(id, "0888")).thenReturn(doctor.getUser());
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(mapper.toDto(any())).thenReturn(new DoctorDto(id, null, null, null, null, "0888", "UID1", true, Set.of()));

        DoctorDto result = doctorService.create(dto);

        verify(keycloakService).assignRealmRole(id, "DOCTOR");
        verify(doctorRepository).save(any(Doctor.class));
        assertThat(result.uniqueId()).isEqualTo("UID1");
    }
}
package com.example.revitaclinic.service;

import com.example.revitaclinic.model.Patient;
import com.example.revitaclinic.repository.PatientRepository;
import com.example.revitaclinic.mapper.PatientMapper;
import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.example.revitaclinic.dto.Patient.CreatePatientDto;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PatientMapper mapper;
    @Mock
    private KeycloakService keycloakService;
    @Mock
    private AppUserService userService;
    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        patient = new Patient();
        patient.setEgn("0123456789");
        patient.setHealthInsuranceLastPayment(LocalDate.now());
        patient.setPersonalDoctor(new Doctor());
        AppUser user = new AppUser();
        user.setKeycloakUserId(UUID.randomUUID());
        patient.setUser(user);
    }

    @Test
    @WithMockUser(authorities = {"DOCTOR"})
    void getEntity() {
        UUID id = patient.getKeycloakUserId();
        given(patientRepository.findByKeycloakUserId(id)).willReturn(Optional.of(patient));
        Patient result = patientService.getEntity(id);
        assertThat(result).isEqualTo(patient);
    }

    @Test
    void create_withOldInsuranceDate_throws() {
        CreatePatientDto dto = new CreatePatientDto(
                UUID.randomUUID(),
                "0888",
                "0123456789",
                LocalDate.now().minusMonths(7),
                UUID.randomUUID()
        );
        assertThatThrownBy(() -> patientService.create(dto))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

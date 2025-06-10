package com.example.revitaclinic.controller;

import com.example.revitaclinic.model.*;
import com.example.revitaclinic.repository.*;
import com.example.revitaclinic.service.KeycloakService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ConsultationControllerIntegrationTest {

    private static final UUID DOCTOR_ID  = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID PATIENT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPg(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        KeycloakService keycloakService() {
            KeycloakService mock = Mockito.mock(KeycloakService.class);
            UserRepresentation ur = new UserRepresentation();
            ur.setFirstName("John");
            ur.setLastName("Doe");
            ur.setEmail("john.doe@example.com");
            Mockito.when(mock.getUser(Mockito.any())).thenReturn(ur);
            Mockito.when(mock.getUserRoles(Mockito.any())).thenReturn(List.of("DOCTOR"));
            return mock;
        }
    }

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ConsultationRepository consultationRepo;
    @Autowired
    DoctorRepository doctorRepo;
    @Autowired
    PatientRepository patientRepo;
    @Autowired
    AppUserRepository appUserRepo;
    @Autowired
    DiagnosisRepository diagnosisRepo;
    @Autowired
    MedicationRepository medicationRepo;

    Consultation consultation;

    @BeforeEach
    void setupData() {
        consultationRepo.deleteAll();
        patientRepo.deleteAll();
        doctorRepo.deleteAll();
        appUserRepo.deleteAll();
        diagnosisRepo.deleteAll();
        medicationRepo.deleteAll();

        AppUser docUser = new AppUser();
        docUser.setKeycloakUserId(DOCTOR_ID);
        docUser.setPhone("1111");
        appUserRepo.save(docUser);

        Doctor doctor = new Doctor();
        doctor.setUser(docUser);
        doctor.setUniqueId("DOC1");
        doctor.setPersonal(true);
        doctorRepo.save(doctor);

        AppUser patientUser = new AppUser();
        patientUser.setKeycloakUserId(PATIENT_ID);
        patientUser.setPhone("2222");
        appUserRepo.save(patientUser);

        Patient patient = new Patient();
        patient.setUser(patientUser);
        patient.setEgn("9001010000");
        patient.setHealthInsuranceLastPayment(LocalDate.now());
        patient.setPersonalDoctor(doctor);
        patientRepo.save(patient);

        Diagnosis diag = new Diagnosis();
        diag.setName("Diag1");
        diag.setDescription("desc");
        diagnosisRepo.save(diag);

        Medication med = new Medication();
        med.setName("Med1");
        med.setDescription("desc");
        medicationRepo.save(med);

        consultation = new Consultation();
        consultation.setDate(LocalDateTime.now());
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        consultation.setDiagnosis(diag);
        consultation.setNotes("notes");
        consultationRepo.save(consultation);
    }

    @Test
    void getAll_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/consultations"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAll_ForbiddenForPatient() throws Exception {
        mockMvc.perform(get("/api/consultations")
                        .with(jwt().authorities(() -> "ROLE_PATIENT")
                                .jwt(jwt -> jwt.subject(PATIENT_ID.toString()))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAll_OkForDoctor() throws Exception {
        mockMvc.perform(get("/api/consultations")
                        .with(jwt().authorities(() -> "ROLE_DOCTOR")
                                .jwt(jwt -> jwt.subject(DOCTOR_ID.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(consultation.getId()));
    }

    @Test
    void getForCurrentPatient_OkForPatient() throws Exception {
        mockMvc.perform(get("/api/consultations/me")
                        .with(jwt().authorities(() -> "ROLE_PATIENT")
                                .jwt(jwt -> jwt.subject(PATIENT_ID.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(consultation.getId()));
    }
}

package com.example.revitaclinic.controller;

import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.model.Doctor;
import com.example.revitaclinic.model.Patient;
import com.example.revitaclinic.repository.AppUserRepository;
import com.example.revitaclinic.repository.DoctorRepository;
import com.example.revitaclinic.repository.PatientRepository;
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
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PatientControllerIntegrationTest {

    private static final UUID DOCTOR_ID  = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID PATIENT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

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
            Mockito.when(mock.getUserRoles(Mockito.any())).thenReturn(List.of("PATIENT"));
            return mock;
        }
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PatientRepository patientRepo;

    @Autowired
    DoctorRepository doctorRepo;

    @Autowired
    AppUserRepository appUserRepo;

    Patient patient;

    @BeforeEach
    void setUpData() {
        patientRepo.deleteAll();
        doctorRepo.deleteAll();
        appUserRepo.deleteAll();

        AppUser docUser = new AppUser();
        docUser.setKeycloakUserId(DOCTOR_ID);
        docUser.setPhone("1111");
        appUserRepo.save(docUser);

        Doctor doc = new Doctor();
        doc.setUser(docUser);
        doc.setUniqueId("DOC1");
        doc.setPersonal(true);
        doctorRepo.save(doc);

        AppUser patientUser = new AppUser();
        patientUser.setKeycloakUserId(PATIENT_ID);
        patientUser.setPhone("2222");
        appUserRepo.save(patientUser);

        Patient p = new Patient();
        p.setUser(patientUser);
        p.setEgn("9001010000");
        p.setHealthInsuranceLastPayment(LocalDate.now());
        p.setPersonalDoctor(doc);
        patient = patientRepo.save(p);
    }

    @Test
    void listAll_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listAll_ForbiddenForPatient() throws Exception {
        mockMvc.perform(get("/api/patients")
                        .with(jwt().authorities(() -> "ROLE_PATIENT")
                                .jwt(jwt -> jwt.subject(PATIENT_ID.toString()))))
                .andExpect(status().isForbidden());
    }

    @Test
    void listAll_OkForDoctor() throws Exception {
        mockMvc.perform(get("/api/patients")
                        .with(jwt().authorities(() -> "ROLE_DOCTOR")
                                .jwt(jwt -> jwt.subject(DOCTOR_ID.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].keycloakUserId").value(PATIENT_ID.toString()));
    }

    @Test
    void getMe_OkForPatient() throws Exception {
        mockMvc.perform(get("/api/patients/me")
                        .with(jwt().authorities(() -> "ROLE_PATIENT")
                                .jwt(jwt -> jwt.subject(PATIENT_ID.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keycloakUserId").value(PATIENT_ID.toString()));
    }

}
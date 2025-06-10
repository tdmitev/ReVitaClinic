package com.example.revitaclinic.integration_tests;

import com.example.revitaclinic.dto.Doctor.CreateDoctorDto;
import com.example.revitaclinic.dto.Patient.CreatePatientDto;
import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.repository.AppUserRepository;
import com.example.revitaclinic.repository.DoctorRepository;
import com.example.revitaclinic.repository.PatientRepository;
import com.example.revitaclinic.service.KeycloakService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegistrationFlowIntegrationTest {

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
            Mockito.when(mock.getUserRoles(Mockito.any())).thenReturn(List.of("DOCTOR", "PATIENT", "ADMIN"));
            Mockito.when(mock.getUser(Mockito.any())).thenReturn(new org.keycloak.representations.idm.UserRepresentation());
            return mock;
        }
    }

    @Autowired
    MockMvc mockMvc;
    @Autowired
    DoctorRepository doctorRepo;
    @Autowired
    PatientRepository patientRepo;
    @Autowired
    AppUserRepository userRepo;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        patientRepo.deleteAll();
        doctorRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void registrationFlow() throws Exception {
        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        CreateDoctorDto docDto = new CreateDoctorDto(doctorId, "0899", "DOC1", false, Set.of());

        mockMvc.perform(post("/api/register/doctor")
                        .with(jwt().authorities(() -> "ROLE_ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/doctors/" + doctorId));

        assertThat(doctorRepo.existsByKeycloakUserId(doctorId)).isTrue();

        CreatePatientDto patientDto = new CreatePatientDto(patientId, "0888", "9001010000", LocalDate.now(), doctorId);

        mockMvc.perform(post("/api/register/patient")
                        .with(jwt().authorities(() -> "ROLE_ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/patients/" + patientId));

        assertThat(patientRepo.existsByKeycloakUserId(patientId)).isTrue();

        mockMvc.perform(get("/api/patients/me")
                        .with(jwt().authorities(() -> "ROLE_PATIENT")
                                .jwt(jwt -> jwt.subject(patientId.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.egn").value("9001010000"));

        mockMvc.perform(get("/api/patients")
                        .with(jwt().authorities(() -> "ROLE_DOCTOR")
                                .jwt(jwt -> jwt.subject(doctorId.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].keycloakUserId").value(patientId.toString()));

        mockMvc.perform(delete("/api/patients/" + patientId)
                        .with(jwt().authorities(() -> "ROLE_DOCTOR")
                                .jwt(jwt -> jwt.subject(doctorId.toString()))))
                .andExpect(status().isNoContent());

        assertThat(patientRepo.existsByKeycloakUserId(patientId)).isFalse();

        mockMvc.perform(delete("/api/doctors/" + doctorId)
                        .with(jwt().authorities(() -> "ROLE_DOCTOR")
                                .jwt(jwt -> jwt.subject(doctorId.toString()))))
                .andExpect(status().isNoContent());

        assertThat(doctorRepo.existsByKeycloakUserId(doctorId)).isFalse();
    }
}

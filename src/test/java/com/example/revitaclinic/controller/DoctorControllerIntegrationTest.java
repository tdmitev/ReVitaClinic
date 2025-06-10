package com.example.revitaclinic.controller;

import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.model.Doctor;
import com.example.revitaclinic.repository.AppUserRepository;
import com.example.revitaclinic.repository.DoctorRepository;
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

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DoctorControllerIntegrationTest {

    private static final UUID DOCTOR_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

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
            ur.setFirstName("Ann");
            ur.setLastName("Smith");
            ur.setEmail("ann@example.com");
            Mockito.when(mock.getUser(Mockito.any())).thenReturn(ur);
            Mockito.when(mock.getUserRoles(Mockito.any())).thenReturn(List.of("DOCTOR"));
            return mock;
        }
    }

    @Autowired
    MockMvc mockMvc;
    @Autowired
    DoctorRepository doctorRepo;
    @Autowired
    AppUserRepository userRepo;

    @BeforeEach
    void setupData() {
        doctorRepo.deleteAll();
        userRepo.deleteAll();

        AppUser user = new AppUser();
        user.setKeycloakUserId(DOCTOR_ID);
        user.setPhone("1234");
        userRepo.save(user);

        Doctor doc = new Doctor();
        doc.setUser(user);
        doc.setUniqueId("DOC1");
        doc.setPersonal(true);
        doctorRepo.save(doc);
    }

    @Test
    void listAll_OkForDoctor() throws Exception {
        mockMvc.perform(get("/api/doctors")
                        .with(jwt().authorities(() -> "ROLE_DOCTOR")
                                .jwt(jwt -> jwt.subject(DOCTOR_ID.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uniqueId").value("DOC1"));
    }

    @Test
    void listAll_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isUnauthorized());
    }
}
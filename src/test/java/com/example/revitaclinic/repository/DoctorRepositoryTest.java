package com.example.revitaclinic.repository;

import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.model.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@ActiveProfiles("test")
class DoctorRepositoryTest {

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


    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void existsByKeycloakUserId() {
        UUID userId = UUID.randomUUID();
        AppUser user = new AppUser();
        user.setKeycloakUserId(userId);
        user.setPhone("123");
        appUserRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setUniqueId("u123");
        doctor.setPersonal(false);

        doctorRepository.save(doctor);

        boolean exists = doctorRepository.existsByKeycloakUserId(userId);
        assertThat(exists).isTrue();
    }
}
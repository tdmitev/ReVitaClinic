package com.example.revitaclinic.repository;

import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.model.Doctor;
import com.example.revitaclinic.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@ActiveProfiles("test")
class PatientRepositoryTest {

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
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void findByPersonalDoctor() {
        UUID docId = UUID.randomUUID();
        AppUser docUser = new AppUser();
        docUser.setKeycloakUserId(docId);
        docUser.setPhone("321");
        appUserRepository.save(docUser);

        Doctor doctor = new Doctor();
        doctor.setUser(docUser);
        doctor.setUniqueId("d1");
        doctor.setPersonal(true);
        doctorRepository.save(doctor);

        UUID patientId = UUID.randomUUID();
        AppUser patientUser = new AppUser();
        patientUser.setKeycloakUserId(patientId);
        patientUser.setPhone("111");
        appUserRepository.save(patientUser);

        Patient patient = new Patient();
        patient.setUser(patientUser);
        patient.setEgn("0123456789");
        patient.setPersonalDoctor(doctor);
        patientRepository.save(patient);

        List<Patient> result = patientRepository.findByPersonalDoctor_KeycloakUserId(docId);
        assertThat(result).hasSize(1);
    }
}
package com.example.revitaclinic.repository;

import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.model.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DoctorRepositoryTest {

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
package com.example.revitaclinic.repository;

import com.example.revitaclinic.model.AppUser;
import com.example.revitaclinic.model.Doctor;
import com.example.revitaclinic.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PatientRepositoryTest {

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
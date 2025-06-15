package com.example.revitaclinic.repository;

import com.example.revitaclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByKeycloakUserId(UUID keycloakUserId);
    boolean existsByKeycloakUserId(UUID keycloakUserId);

    List<Patient> findByPersonalDoctor_KeycloakUserId(UUID doctorKeycloakUserId);

    @Query("SELECT p.personalDoctor.keycloakUserId, COUNT(p) FROM Patient p GROUP BY p.personalDoctor.keycloakUserId")
    List<Object[]> countPatientsPerDoctor();
}

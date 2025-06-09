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

    @Query(value = "SELECT personal_doctor_id, COUNT(*) FROM revitaclinic.patient GROUP BY personal_doctor_id", nativeQuery = true)
    List<Object[]> countPatientsPerDoctor();
}

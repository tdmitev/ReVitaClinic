package com.example.revitaclinic.repository;

import com.example.revitaclinic.model.Consultation;
import com.example.revitaclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {
    List<Consultation> findByPatient_KeycloakUserId(UUID patientId);
    List<Consultation> findByDoctor_KeycloakUserId(UUID doctorId);
    List<Consultation> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<Consultation> findByDoctor_KeycloakUserIdAndDateBetween(UUID doctorId, LocalDateTime start, LocalDateTime end);

    @Query("select c.patient from Consultation c where c.diagnosis.id = :diagnosisId")
    List<Patient> findPatientsByDiagnosis(Integer diagnosisId);

    @Query("SELECT c.doctor.keycloakUserId, COUNT(c) FROM Consultation c GROUP BY c.doctor.keycloakUserId")
    List<Object[]> countConsultationsPerDoctor();

    @Query("SELECT c.doctor.keycloakUserId FROM Consultation c JOIN c.sickLeave sl GROUP BY c.doctor.keycloakUserId ORDER BY COUNT(sl) DESC")
    List<UUID> doctorWithMostSickLeaves(org.springframework.data.domain.Pageable pageable);
}
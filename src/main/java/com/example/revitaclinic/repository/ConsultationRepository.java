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

    @Query("select c.patient from Consultation c where c.diagnosis.id = :diagnosisId")
    List<Patient> findPatientsByDiagnosis(Integer diagnosisId);

    @Query(value = "SELECT doctor_id, COUNT(*) FROM revitaclinic.consultation GROUP BY doctor_id", nativeQuery = true)
    List<Object[]> countConsultationsPerDoctor();

    @Query(value = "SELECT c.doctor_id FROM revitaclinic.consultation c JOIN revitaclinic.sick_leave sl ON c.id = sl.consultation_id GROUP BY c.doctor_id ORDER BY COUNT(*) DESC LIMIT 1", nativeQuery = true)
    UUID doctorWithMostSickLeaves();
}
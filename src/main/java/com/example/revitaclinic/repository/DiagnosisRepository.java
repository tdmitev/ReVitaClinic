package com.example.revitaclinic.repository;

import com.example.revitaclinic.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Integer> {
    List<Diagnosis> findAllByMedications_Id(Integer medicationId);

    @Query(value = "SELECT diagnosis_id FROM revitaclinic.consultation GROUP BY diagnosis_id ORDER BY COUNT(*) DESC LIMIT 1", nativeQuery = true)
    Optional<Integer> mostCommonDiagnosisId();
}

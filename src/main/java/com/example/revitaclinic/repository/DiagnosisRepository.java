package com.example.revitaclinic.repository;

import com.example.revitaclinic.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Integer> {
    List<Diagnosis> findAllByMedications_Id(Integer medicationId);
}

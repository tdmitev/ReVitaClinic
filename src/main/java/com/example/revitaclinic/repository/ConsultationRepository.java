package com.example.revitaclinic.repository;

import com.example.revitaclinic.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {
}
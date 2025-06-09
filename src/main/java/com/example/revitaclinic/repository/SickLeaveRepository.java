package com.example.revitaclinic.repository;

import com.example.revitaclinic.model.SickLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SickLeaveRepository extends JpaRepository<SickLeave, Integer> {
    @Query(value = "SELECT EXTRACT(MONTH FROM start_date) FROM revitaclinic.sick_leave GROUP BY EXTRACT(MONTH FROM start_date) ORDER BY COUNT(*) DESC LIMIT 1", nativeQuery = true)
    Optional<Integer> monthWithMostSickLeaves();
}

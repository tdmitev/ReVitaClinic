package com.example.revitaclinic.controller;

import com.example.revitaclinic.dto.Diagnosis.DiagnosisDto;
import com.example.revitaclinic.dto.Doctor.DoctorDto;
import com.example.revitaclinic.dto.Patient.PatientDto;
import com.example.revitaclinic.dto.Stats.DoctorCountDto;
import com.example.revitaclinic.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stats")
@PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
public class StatisticsController {
    private final StatisticsService service;

    public StatisticsController(StatisticsService service) {
        this.service = service;
    }

    @GetMapping("/patients-by-diagnosis/{id}")
    public List<PatientDto> patientsByDiagnosis(@PathVariable Integer id) {
        return service.patientsByDiagnosis(id);
    }

    @GetMapping("/patients-by-doctor/{id}")
    public List<PatientDto> patientsByDoctor(@PathVariable UUID id) {
        return service.patientsByDoctor(id);
    }

    @GetMapping("/most-common-diagnosis")
    public DiagnosisDto mostCommonDiagnosis() {
        return service.mostCommonDiagnosis();
    }

    @GetMapping("/patient-count-per-doctor")
    public List<DoctorCountDto> patientCountPerDoctor() {
        return service.patientCountPerDoctor();
    }

    @GetMapping("/consultation-count-per-doctor")
    public List<DoctorCountDto> consultationCountPerDoctor() {
        return service.consultationCountPerDoctor();
    }

    @GetMapping("/month-most-sick-leaves")
    public ResponseEntity<Integer> monthMostSickLeaves() {
        return ResponseEntity.ok(service.monthWithMostSickLeaves());
    }

    @GetMapping("/doctor-most-sick-leaves")
    public DoctorDto doctorMostSickLeaves() {
        return service.doctorWithMostSickLeaves();
    }
}

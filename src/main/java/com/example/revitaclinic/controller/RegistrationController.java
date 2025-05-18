package com.example.revitaclinic.controller;

import com.example.revitaclinic.dto.Doctor.CreateDoctorDto;
import com.example.revitaclinic.dto.Doctor.DoctorDto;
import com.example.revitaclinic.dto.Patient.CreatePatientDto;
import com.example.revitaclinic.dto.Patient.PatientDto;
import com.example.revitaclinic.service.DoctorService;
import com.example.revitaclinic.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/register")
@PreAuthorize("hasRole('ADMIN')")
public class RegistrationController {

    private final DoctorService doctorService;
    private final PatientService patientService;

    public RegistrationController(DoctorService doctorService,
                                  PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @PostMapping("/doctor")
    public ResponseEntity<DoctorDto> registerDoctor(
            @Valid @RequestBody CreateDoctorDto dto) {
        DoctorDto created = doctorService.create(dto);
        URI uri = URI.create("/api/doctors/" + created.keycloakUserId());
        return ResponseEntity.created(uri).body(created);
    }

    @PostMapping("/patient")
    public ResponseEntity<PatientDto> registerPatient(
            @Valid @RequestBody CreatePatientDto dto) {
        PatientDto created = patientService.create(dto);
        URI uri = URI.create("/api/patients/" + created.keycloakUserId());
        return ResponseEntity.created(uri).body(created);
    }
}
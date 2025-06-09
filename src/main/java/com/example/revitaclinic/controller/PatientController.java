package com.example.revitaclinic.controller;

import com.example.revitaclinic.dto.Patient.PatientDto;
import com.example.revitaclinic.dto.Patient.UpdatePatientDto;
import com.example.revitaclinic.service.PatientService;
import com.example.revitaclinic.config.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> listAll() {
        List<PatientDto> all = patientService.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientDto> getMe() {
        UUID id = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(patientService.findById(id));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientDto> updateMe(@Valid @RequestBody UpdatePatientDto dto) {
        UUID id = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(patientService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getOne(@PathVariable UUID id) {
        PatientDto dto = patientService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePatientDto dto) {
        PatientDto updated = patientService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
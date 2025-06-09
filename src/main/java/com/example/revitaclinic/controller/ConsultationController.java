package com.example.revitaclinic.controller;

import com.example.revitaclinic.dto.Consultation.ConsultationDto;
import com.example.revitaclinic.dto.Consultation.CreateConsultationDto;
import com.example.revitaclinic.dto.Consultation.UpdateConsultationDto;
import com.example.revitaclinic.service.ConsultationService;
import com.example.revitaclinic.config.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
@RequestMapping("/api/consultations")
@Validated
public class ConsultationController {

    private final ConsultationService service;

    public ConsultationController(ConsultationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ConsultationDto> create(
            @RequestBody @Valid CreateConsultationDto dto
    ) {
        ConsultationDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ConsultationDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<ConsultationDto>> getForCurrentPatient() {
        UUID id = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.findByPatient(id));
    }

    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<ConsultationDto>> getByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(service.findByPatient(patientId));
    }

    @GetMapping("/by-doctor/{doctorId}")
    public ResponseEntity<List<ConsultationDto>> getByDoctor(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(service.findByDoctor(doctorId));
    }

    @GetMapping("/period")
    public ResponseEntity<List<ConsultationDto>> getByPeriod(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        return ResponseEntity.ok(service.findByPeriod(start, end));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultationDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateConsultationDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

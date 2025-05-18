package com.example.revitaclinic.controller;

import com.example.revitaclinic.dto.Consultation.ConsultationDto;
import com.example.revitaclinic.dto.Consultation.CreateConsultationDto;
import com.example.revitaclinic.dto.Consultation.UpdateConsultationDto;
import com.example.revitaclinic.service.ConsultationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

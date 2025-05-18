package com.example.revitaclinic.controller;

import com.example.revitaclinic.dto.*;
import com.example.revitaclinic.service.DiagnosisService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/diagnoses")
@PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
public class DiagnosisController {

    private final DiagnosisService service;

    public DiagnosisController(DiagnosisService service) {
        this.service = service;
    }

    @PostMapping
    public DiagnosisDto create(@RequestBody @Valid CreateDiagnosisDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<DiagnosisDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public DiagnosisDto findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public DiagnosisDto update(@PathVariable Integer id,
                               @RequestBody @Valid UpdateDiagnosisDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
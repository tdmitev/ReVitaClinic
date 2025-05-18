package com.example.revitaclinic.controller;

import com.example.revitaclinic.dto.Medication.CreateMedicationDto;
import com.example.revitaclinic.dto.Medication.MedicationDto;
import com.example.revitaclinic.dto.Medication.UpdateMedicationDto;
import com.example.revitaclinic.service.MedicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medications")
@PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
public class MedicationController {

    private final MedicationService service;

    public MedicationController(MedicationService service) {
        this.service = service;
    }

    @PostMapping
    public MedicationDto create(@RequestBody @Valid CreateMedicationDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<MedicationDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MedicationDto findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public MedicationDto update(@PathVariable Integer id,
                                @RequestBody @Valid UpdateMedicationDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
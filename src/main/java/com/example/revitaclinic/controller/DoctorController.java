package com.example.revitaclinic.controller;

import com.example.revitaclinic.dto.DoctorDto;
import com.example.revitaclinic.dto.UpdateDoctorDto;
import com.example.revitaclinic.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorDto>> listAll() {
        List<DoctorDto> all = doctorService.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getOne(@PathVariable UUID id) {
        DoctorDto dto = doctorService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDoctorDto dto) {
        DoctorDto updated = doctorService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
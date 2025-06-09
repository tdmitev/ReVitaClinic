package com.example.revitaclinic.controller;

import com.example.revitaclinic.dto.SickLeave.CreateSickLeaveDto;
import com.example.revitaclinic.dto.SickLeave.SickLeaveDto;
import com.example.revitaclinic.service.SickLeaveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sick-leaves")
@PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
public class SickLeaveController {
    private final SickLeaveService service;

    public SickLeaveController(SickLeaveService service) {
        this.service = service;
    }

    @PostMapping("/{consultationId}")
    public ResponseEntity<SickLeaveDto> create(@PathVariable Integer consultationId,
                                               @Valid @RequestBody CreateSickLeaveDto dto) {
        SickLeaveDto created = service.create(consultationId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public SickLeaveDto getOne(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public SickLeaveDto update(@PathVariable Integer id,
                               @Valid @RequestBody CreateSickLeaveDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

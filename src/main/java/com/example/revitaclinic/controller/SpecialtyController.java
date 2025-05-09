package com.example.revitaclinic.controller;

import com.example.revitaclinic.dto.*;
import com.example.revitaclinic.service.SpecialtyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/specialties")
public class SpecialtyController {

    private final SpecialtyService service;

    public SpecialtyController(SpecialtyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SpecialtyDto> create(
            @Valid @RequestBody CreateSpecialtyDto dto) {
        SpecialtyDto created = service.create(dto);
        return ResponseEntity
                .created(URI.create("/api/specialties/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public SpecialtyDto getById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @GetMapping
    public List<SpecialtyDto> getAll() {
        return service.findAll();
    }

    @PutMapping("/{id}")
    public SpecialtyDto update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateSpecialtyDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.example.revitaclinic.dto.Specialty;

import jakarta.validation.constraints.Size;

public record UpdateSpecialtyDto(
        @Size(max = 255) String name
) {}

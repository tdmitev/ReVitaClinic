package com.example.revitaclinic.dto.Specialty;

import jakarta.validation.constraints.NotBlank;

public record CreateSpecialtyDto(
        @NotBlank
        String name
) {}
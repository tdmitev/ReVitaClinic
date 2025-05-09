package com.example.revitaclinic.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSpecialtyDto(
        @NotBlank
        String name
) {}
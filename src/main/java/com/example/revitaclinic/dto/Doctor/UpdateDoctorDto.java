package com.example.revitaclinic.dto.Doctor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateDoctorDto(
        @NotNull(message = "Phone is required")
        @Size(max = 50) String phone,
        @NotNull(message = "Unique ID is required")
        @Size(max = 50) String uniqueId,
        Boolean personal,
        Set<Integer> specialtyIds
) {}
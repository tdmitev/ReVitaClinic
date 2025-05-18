package com.example.revitaclinic.dto.Doctor;

import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateDoctorDto(
        @Size(max = 50) String phone,
        @Size(max = 50) String uniqueId,
        Boolean personal,
        Set<Integer> specialtyIds
) {}
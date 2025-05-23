package com.example.revitaclinic.dto.Doctor;

import com.example.revitaclinic.dto.Specialty.SpecialtyDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record DoctorDto(
        UUID keycloakUserId,
        String firstName,
        String lastName,
        String email,
        List<String> roles,
        String phone,
        String uniqueId,
        boolean personal,
        Set<SpecialtyDto> specialties    // ново поле
) {}

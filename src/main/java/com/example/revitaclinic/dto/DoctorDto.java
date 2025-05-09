package com.example.revitaclinic.dto;

import java.util.List;
import java.util.UUID;

public record DoctorDto(
        UUID keycloakUserId,
        String firstName,
        String lastName,
        String email,
        List<String> roles,
        String phone,
        String uniqueId,
        boolean personal
) {}

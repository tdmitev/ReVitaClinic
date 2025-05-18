package com.example.revitaclinic.dto.Doctor;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CreateDoctorDto(
        @NotNull UUID keycloakUserId,
        @NotNull String phone,
        @NotNull String uniqueId,
        boolean personal,
        Set<@NotNull Integer> specialtyIds   // списък със зададени specialty IDs
) {}

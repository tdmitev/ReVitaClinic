package com.example.revitaclinic.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDoctorDto(
        @NotNull UUID keycloakUserId,
        @NotNull String phone,
        @NotNull String uniqueId,
        boolean personal
) {}

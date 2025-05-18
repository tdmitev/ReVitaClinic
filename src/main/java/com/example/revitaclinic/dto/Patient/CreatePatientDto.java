package com.example.revitaclinic.dto.Patient;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record CreatePatientDto(
        @NotNull UUID keycloakUserId,
        @NotNull String phone,
        @NotNull String egn,
        LocalDate healthInsuranceLastPayment,
        @NotNull UUID personalDoctorId
) {}
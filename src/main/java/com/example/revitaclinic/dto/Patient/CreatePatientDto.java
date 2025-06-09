package com.example.revitaclinic.dto.Patient;

import com.example.revitaclinic.constraints.ValidEgn;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record CreatePatientDto(
        @NotNull UUID keycloakUserId,
        @NotNull @Size(max = 50) String phone,
        @NotNull @ValidEgn String egn,
        LocalDate healthInsuranceLastPayment,
        @NotNull UUID personalDoctorId
) {}
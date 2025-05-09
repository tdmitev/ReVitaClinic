package com.example.revitaclinic.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PatientDto(
        UUID keycloakUserId,
        String firstName,
        String lastName,
        String email,
        List<String> roles,
        String phone,
        String egn,
        LocalDate healthInsuranceLastPayment,
        UUID personalDoctorId
) {}
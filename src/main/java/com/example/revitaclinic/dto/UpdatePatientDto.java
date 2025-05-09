package com.example.revitaclinic.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UpdatePatientDto(
        String phone,
        String egn,
        LocalDate healthInsuranceLastPayment,
        UUID personalDoctorId
) {}

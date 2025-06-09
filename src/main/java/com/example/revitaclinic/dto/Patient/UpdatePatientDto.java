package com.example.revitaclinic.dto.Patient;

import com.example.revitaclinic.constraints.ValidEgn;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record UpdatePatientDto(
        @Size(max = 50) String phone,
        @ValidEgn String egn,
        LocalDate healthInsuranceLastPayment,
        UUID personalDoctorId
) {}

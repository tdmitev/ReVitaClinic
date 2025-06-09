package com.example.revitaclinic.dto.SickLeave;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateSickLeaveDto(
        @NotNull LocalDate startDate,
        @NotNull Integer numberOfDays
) {}
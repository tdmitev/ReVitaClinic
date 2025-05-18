package com.example.revitaclinic.dto.SickLeave;

import java.time.LocalDate;

public record CreateSickLeaveDto(
        LocalDate startDate,
        Integer numberOfDays
) {}
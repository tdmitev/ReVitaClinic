package com.example.revitaclinic.dto.SickLeave;

import java.time.LocalDate;

public record SickLeaveDto(
        LocalDate startDate,
        Integer numberOfDays
) {}

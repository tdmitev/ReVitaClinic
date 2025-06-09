package com.example.revitaclinic.dto.Diagnosis;

import jakarta.validation.constraints.NotNull;

public record DiagnosisSummaryDto(
        @NotNull(message = "ID is required")
        Integer id,
        @NotNull(message = "Name is required")
        String name
) {}
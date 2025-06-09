package com.example.revitaclinic.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEgnValidator implements ConstraintValidator<ValidEgn, String> {
    private static final String EGN_REGEX = "\\d{10}";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches(EGN_REGEX);
    }
}
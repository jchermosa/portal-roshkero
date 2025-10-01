package com.backend.portalroshkabackend.Exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EstadoValidator implements ConstraintValidator<ValidEstado, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value.equals("A") || value.equals("I");
    }
}
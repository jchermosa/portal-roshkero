package com.backend.portalroshkabackend.tools.validator;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EstadoValidator implements ConstraintValidator<ValidEstado, EstadoActivoInactivo> {
    @Override
    public boolean isValid(EstadoActivoInactivo value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value == EstadoActivoInactivo.A || value == EstadoActivoInactivo.I;
    }
}
package com.backend.portalroshkabackend.Exception;

import com.backend.portalroshkabackend.Repositories.EquiposRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class NombreUnicoValidator implements ConstraintValidator<UniqueNombre, String> {

    @Autowired
    private EquiposRepository equiposRepository;

    @Override
    public boolean isValid(String nombre, ConstraintValidatorContext context) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return true; // пусть проверку пустоты делает @NotBlank
        }
        return !equiposRepository.existsByNombre(nombre);
    }
}
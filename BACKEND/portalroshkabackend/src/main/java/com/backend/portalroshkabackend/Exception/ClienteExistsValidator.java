package com.backend.portalroshkabackend.Exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.portalroshkabackend.Repositories.ClientesRepository;

public class ClienteExistsValidator implements ConstraintValidator<ClienteExists, Integer> {

    @Autowired
    private ClientesRepository clienteRepository;

    @Override
    public boolean isValid(Integer idCliente, ConstraintValidatorContext context) {
        if (idCliente == null) return false;
        return clienteRepository.existsById(idCliente);
    }
}
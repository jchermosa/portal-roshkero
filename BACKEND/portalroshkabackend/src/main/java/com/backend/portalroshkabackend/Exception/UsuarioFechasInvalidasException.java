package com.backend.portalroshkabackend.Exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsuarioFechasInvalidasException extends RuntimeException {
    private final List<String> errores;

    public UsuarioFechasInvalidasException(List<String> errores) {
        super("Fechas inválidas para usuarios");
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}

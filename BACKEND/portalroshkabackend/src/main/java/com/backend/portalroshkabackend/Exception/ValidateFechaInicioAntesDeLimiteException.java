package com.backend.portalroshkabackend.Exception;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidateFechaInicioAntesDeLimiteException extends RuntimeException {
    public ValidateFechaInicioAntesDeLimiteException(String message) {
        super(message);
    }
}

package com.backend.portalroshkabackend.Exception;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        body.put("message", "Validación fallida");
        body.put("errors", errors);
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        Map<String, String> errors = new HashMap<>();

        if (ex.getTargetType().isEnum()) {
            errors.put("estado", "Solo se permite A o I");
        } else {
            errors.put("value", "Formato inválido");
        }

        body.put("message", "Validación fallida");
        body.put("errors", errors);
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        Map<String, String> errors = new HashMap<>();

        if (ex.getReason() != null) { // проверяем на null
            String[] parts = ex.getReason().split(":", 2);
            if (parts.length == 2) {
                errors.put(parts[0].trim(), parts[1].trim());
            } else {
                errors.put("error", ex.getReason());
            }
        }

        body.put("message", "Validación fallida");
        body.put("errors", errors);
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatusCode().value());

        return new ResponseEntity<>(body, ex.getStatusCode());
    }

}

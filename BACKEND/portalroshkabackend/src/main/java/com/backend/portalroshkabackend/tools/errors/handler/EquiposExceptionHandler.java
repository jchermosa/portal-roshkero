package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.Operationes.UsersEquipoErrores;
import com.backend.portalroshkabackend.DTO.common.ErrorResponseDto;
import com.backend.portalroshkabackend.Exception.DisponibilidadInsuficienteException;
import com.backend.portalroshkabackend.Exception.NombreDuplicadoException;
import com.backend.portalroshkabackend.Exception.ValidateFechaInicioAntesDeLimiteException;
import com.backend.portalroshkabackend.Exception.UsuarioFechasInvalidasException;
import com.backend.portalroshkabackend.tools.errors.errorslist.equipos.EquipoNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class EquiposExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(EquipoNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEquipoNotFound(EquipoNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(NombreDuplicadoException.class)
    public ResponseEntity<ErrorResponseDto> handleNombreDuplicado(NombreDuplicadoException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DisponibilidadInsuficienteException.class)
    public ResponseEntity<ErrorResponseDto> handleNombreDuplicado(DisponibilidadInsuficienteException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ValidateFechaInicioAntesDeLimiteException.class)
    public ResponseEntity<ErrorResponseDto> ValidateFechaInicioAntesDeLimite(
            ValidateFechaInicioAntesDeLimiteException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UsuarioFechasInvalidasException.class)
    public ResponseEntity<UsersEquipoErrores> handleUsuarioFechasInvalidas(UsuarioFechasInvalidasException ex) {
        UsersEquipoErrores response = new UsersEquipoErrores();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Errores en las fechas de los usuarios");
        response.setDetails(ex.getErrores()); 
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    
}

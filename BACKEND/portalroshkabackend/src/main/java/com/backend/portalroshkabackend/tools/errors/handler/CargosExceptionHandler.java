package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.common.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoAlreadyInactiveException;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoAssignedToUsersException;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoDuplicateNameException;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class CargosExceptionHandler extends BaseExceptionHandler{

    @ExceptionHandler(CargoNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePositionNotFound(CargoNotFoundException ex){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CargoAlreadyInactiveException.class)
    public ResponseEntity<ErrorResponseDto> handlePositionAlreadyInactive(CargoAlreadyInactiveException ex){
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CargoAssignedToUsersException.class)
    public ResponseEntity<ErrorResponseDto> handleCargoAssignedToUsers(CargoAssignedToUsersException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CargoDuplicateNameException.class)
    public ResponseEntity<ErrorResponseDto> handleCargoDuplicateName(CargoDuplicateNameException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }
}

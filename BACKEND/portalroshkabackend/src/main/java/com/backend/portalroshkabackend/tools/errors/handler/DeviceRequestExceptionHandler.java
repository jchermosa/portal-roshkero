package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.common.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudDispositivos.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class DeviceRequestExceptionHandler extends BaseExceptionHandler{
    @ExceptionHandler(DeviceRequestNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleDeviceRequestNotFoundException(DeviceRequestNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AlreadyCheckedRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleAlreadyCheckedRequestException(AlreadyCheckedRequestException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidRequestStateException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRequestStateException(InvalidRequestStateException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidCommentFormatDRException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCommentFormatDRException(InvalidCommentFormatDRException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CommentDRParsingException.class)
    public ResponseEntity<ErrorResponseDto> handleCommentDRParsingException(CommentDRParsingException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler(DeviceRequestProcessingException.class)
    public ResponseEntity<ErrorResponseDto> handleDeviceRequestProcessingException(DeviceRequestProcessingException ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}

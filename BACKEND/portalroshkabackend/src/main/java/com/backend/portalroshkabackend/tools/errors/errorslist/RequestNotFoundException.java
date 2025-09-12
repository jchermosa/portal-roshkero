package com.backend.portalroshkabackend.tools.errors.errorslist;

public class RequestNotFoundException extends RuntimeException{
    public RequestNotFoundException(int id){
        super("Solicitud con ID " + " no encontrado.");
    }
}

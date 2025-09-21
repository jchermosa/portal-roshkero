package com.backend.portalroshkabackend.tools.errors.errorslist;

public class UserHavePendientRequestsException extends RuntimeException{
    public UserHavePendientRequestsException(){
        super("El usuario tiene solicitudes pendientes.");
    }
}

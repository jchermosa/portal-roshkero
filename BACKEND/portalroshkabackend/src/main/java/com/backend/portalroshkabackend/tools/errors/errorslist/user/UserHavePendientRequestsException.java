package com.backend.portalroshkabackend.tools.errors.errorslist.user;

public class UserHavePendientRequestsException extends RuntimeException{
    public UserHavePendientRequestsException(String username){
        super("El usuario " + username +" tiene solicitudes pendientes.");
    }
}

package com.backend.portalroshkabackend.tools.errors.errorslist;

public class RequestAlreadyAcceptedException extends RuntimeException{
    public RequestAlreadyAcceptedException(int idSolicitud){
        super("La solicitud con ID " + idSolicitud + " ya ha sido aceptada");
    }

    public RequestAlreadyAcceptedException(String messageCustomized){
        super(messageCustomized);
    }
}

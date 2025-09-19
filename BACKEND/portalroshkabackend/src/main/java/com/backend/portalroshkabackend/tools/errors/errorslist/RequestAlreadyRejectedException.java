package com.backend.portalroshkabackend.tools.errors.errorslist;

public class RequestAlreadyRejectedException extends RuntimeException{
    public RequestAlreadyRejectedException(int idSolicitud){
        super("La solicitud con ID " + idSolicitud + " ya se encuentra rechazada.");
    }

    public RequestAlreadyRejectedException(String messageCustomized){
        super(messageCustomized);
    }
}

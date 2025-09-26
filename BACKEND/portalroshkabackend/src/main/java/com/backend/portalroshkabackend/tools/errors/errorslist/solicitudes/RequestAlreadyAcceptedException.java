package com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes;

public class RequestAlreadyAcceptedException extends RuntimeException{
    public RequestAlreadyAcceptedException(int idSolicitud){
        super("La solicitud con ID " + idSolicitud + " ya ha sido aceptada");
    }

}

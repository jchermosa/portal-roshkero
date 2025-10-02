package com.backend.portalroshkabackend.tools.errors.errorslist.asignacionDispositivos;

import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;

public class InvalidRequestTypeException extends RuntimeException {
    public InvalidRequestTypeException(SolicitudesEnum tipoSolicitud) {
        super("La solicitud no es del tipo DISPOSITIVO es del tipo:" + tipoSolicitud);
    }
}

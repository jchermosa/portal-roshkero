package com.backend.portalroshkabackend.tools.validator;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Repositories.SolicitudRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestAlreadyAcceptedException;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestAlreadyRejectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestValidator {

    private final SolicitudRepository solicitudRepository;

    @Autowired
    public RequestValidator(SolicitudRepository solicitudRepository){
        this.solicitudRepository = solicitudRepository;
    }

    public void validateRequestStatus(EstadoSolicitudEnum estado, Integer idRequest){
        if (estado == EstadoSolicitudEnum.R) throw new RequestAlreadyRejectedException(idRequest);
        if (estado == EstadoSolicitudEnum.A) throw new RequestAlreadyAcceptedException(idRequest);
    }
}

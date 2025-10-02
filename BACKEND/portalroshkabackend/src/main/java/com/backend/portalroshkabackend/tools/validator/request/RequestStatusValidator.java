package com.backend.portalroshkabackend.tools.validator.request;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Repositories.TH.SolicitudRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestAlreadyAcceptedException;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestAlreadyRejectedException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestStatusValidator implements ValidatorStrategy<Solicitud> {

    @Override
    public void validate(Solicitud solicitud) {
        if (solicitud.getEstado() == EstadoSolicitudEnum.R) throw new RequestAlreadyRejectedException(solicitud.getIdSolicitud());

        if (solicitud.getEstado() == EstadoSolicitudEnum.A) throw new RequestAlreadyAcceptedException(solicitud.getIdSolicitud());

    }
}

package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.self.RequestResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Repositories.*;
import com.backend.portalroshkabackend.tools.SaveManager;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.AutoMap;
import com.backend.portalroshkabackend.tools.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestServiceImpl implements IRequestService{
    private final SolicitudesTHRepository solicitudesTHRepository;

    private final RequestValidator requestValidator;

    @Autowired
    public RequestServiceImpl (SolicitudesTHRepository solicitudesTHRepository,

                              RequestValidator requestValidator){

        this.solicitudesTHRepository = solicitudesTHRepository;

        this.requestValidator = requestValidator;
    }

    // @Transactional(readOnly = true)
    // @Override
    // public Page<SolicitudTHResponseDto> getApprovedByLeader(Pageable pageable) {
    //     Page<Solicitud> requests = solicitudesTHRepository.findAllByEstadoLiderAndEstado(EstadoSolicitudEnum.P, EstadoSolicitudEnum.P, pageable);
    //     return requests.map(AutoMap::toSolicitudTHResponseDto);
    // }

    @Transactional(readOnly = true)
    @Override
    public Page<SolicitudTHResponseDto> getByEstado(EstadoSolicitudEnum estado, Pageable pageable) {
        Page<Solicitud> requestSorted = solicitudesTHRepository.findAllByEstado(estado, pageable);

        return requestSorted.map(AutoMap::toSolicitudTHResponseDto);
    }

    @Transactional
    @Override
    public RequestResponseDto acceptRequest(int idRequest) {
        Solicitud request = solicitudesTHRepository.findById(idRequest).orElseThrow(() -> new RequestNotFoundException(idRequest));

        requestValidator.validateRequestStatus(request.getEstado(), request.getIdSolicitud());

        request.setEstado(EstadoSolicitudEnum.A);

        Solicitud acceptedRequest = SaveManager.saveEntity( () -> solicitudesTHRepository.save(request), "Error al aceptar la solicitud: ");

        return AutoMap.toRequestResponseDto(acceptedRequest.getIdSolicitud(), "Solicitud aceptada.");
    }

    @Transactional
    @Override
    public RequestResponseDto rejectRequest(int idRequest) {
        Solicitud request = solicitudesTHRepository.findById(idRequest).orElseThrow(() -> new RequestNotFoundException(idRequest));

        requestValidator.validateRequestStatus(request.getEstado(), request.getIdSolicitud());

        request.setEstado(EstadoSolicitudEnum.R); // Setea la solicitud como rechazada

        Solicitud rejectedRequest =  SaveManager.saveEntity( () -> solicitudesTHRepository.save(request), "Error al rechazar la solicitud: ");

        return AutoMap.toRequestResponseDto(rejectedRequest.getIdSolicitud(), "Solicitud rechazada");

    }

    
}

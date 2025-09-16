package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.RequestRejectedDto;
import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.Models.Beneficios;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Solicitudes;
import com.backend.portalroshkabackend.Models.SolicitudesTH;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Repositories.*;
import com.backend.portalroshkabackend.tools.SaveManager;
import com.backend.portalroshkabackend.tools.errors.errorslist.RequestNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.AutoMap;
import com.backend.portalroshkabackend.tools.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RequestServiceImpl implements IRequestService{
    private final RequestRepository requestRepository;
    private final SolicitudesTHRepository solicitudesTHRepository;
    private final BeneficiosRepository beneficiosRepository;
    private final TipoDispositivoRepository tipoDispositivoRepository;
    private final SolicitudTHTipoRepository solicitudTHTipoRepository;
    private final PermisosRepository permisosRepository;

    private final Validator validator;

    @Autowired
    public RequestServiceImpl (RequestRepository requestRepository,
                              SolicitudesTHRepository solicitudesTHRepository,
                              BeneficiosRepository beneficiosRepository,
                              TipoDispositivoRepository tipoDispositivoRepository,
                              SolicitudTHTipoRepository solicitudTHTipoRepository,
                              PermisosRepository permisosRepository,
                              Validator validator){
        this.requestRepository = requestRepository;
        this.solicitudesTHRepository = solicitudesTHRepository;
        this.beneficiosRepository = beneficiosRepository;
        this.tipoDispositivoRepository = tipoDispositivoRepository;
        this.solicitudTHTipoRepository = solicitudTHTipoRepository;
        this.permisosRepository = permisosRepository;

        this.validator = validator;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RequestDto> getAllRequests(Pageable pageable) {
        Page<Solicitudes> requests = requestRepository.findAll(pageable);
        return requests.map(AutoMap::toRequestDto); // Retorna todas las solicitudes (DTOs)
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SolicitudTHResponseDto> getApprovedByLeader(Pageable pageable) {
        Page<SolicitudesTH> requests = solicitudesTHRepository.findAllByEstadoLiderAndEstadoTh(EstadoSolicitudEnum.P, EstadoSolicitudEnum.P, pageable);
        return requests.map(AutoMap::toSolicitudTHResponseDto);
    }



    @Override
    public Page<SolicitudTHResponseDto> getByEstado(EstadoSolicitudEnum estado, Pageable pageable) {
        Page<SolicitudesTH> requestSorted = solicitudesTHRepository.findAllByEstado(estado, pageable);

        return requestSorted.map(AutoMap::toSolicitudTHResponseDto);
    }


    @Transactional
    @Override
    public boolean acceptRequest(int idRequest) {
        Solicitudes request = requestRepository.findById(idRequest)
                .orElseThrow(() -> new RequestNotFoundException(idRequest));

        // Si se acepta la solicitud, rechazado y estado de la solicitu se setea a false
        request.setEstado(EstadoSolicitudEnum.A);

        return true;
    }

    @Transactional
    @Override
    public boolean rejectRequest(int idRequest, RequestRejectedDto rejectedDto) {
        Solicitudes request = requestRepository.findById(idRequest)
                .orElseThrow(() -> new RequestNotFoundException(idRequest));

        request.setEstado(EstadoSolicitudEnum.R); // Setea la solicitud como rechazada
        request.setComentario(rejectedDto.getComentario());

        SaveManager.saveEntity( () -> requestRepository.save(request), "Error al rechazar la solicitud: ");
        return true;

    }

    @Transactional
    @Override
    public RequestDto addNewRequestType() {
        return null;
    }
}

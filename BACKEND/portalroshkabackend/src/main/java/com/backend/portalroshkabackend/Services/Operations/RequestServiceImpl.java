package com.backend.portalroshkabackend.Services.Operations;

import com.backend.portalroshkabackend.DTO.Operationes.RequestResponseDto;
import com.backend.portalroshkabackend.Repositories.RequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements IRequestService {
    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;

    }

    @Override
    public Page<RequestResponseDto> getAllRequests(Pageable pageable) {
        return requestRepository.findAll(pageable)
                .map(request -> {
                    RequestResponseDto dto = new RequestResponseDto();
                    dto.setIdSolicitud(request.getIdSolicitud());
                    dto.setFechaInicio(request.getFechaInicio());
                    dto.setFechaFin(request.getFechaFin());
                    dto.setIdUsuario(request.getIdUsuario());
                    dto.setCantidadDias(request.getCantidadDias());
                    dto.setAprobationTh(request.isAprobationTh());
                    // dto.setNumeroAprobaciones(request.getNumeroAprobaciones());
                    dto.setComentario(request.getComentario());
                    dto.setidSolicitudTipo(request.getIdSolicitudTipo());
                    dto.setEstado(request.getEstado());
                    dto.setFechaCreacion(request.getFechaCreacion());
                    dto.setIdPermiso(request.getIdPermiso());
                    dto.setIdBenefito(request.getIdBenefito());

                    return dto;
                });

    }
}

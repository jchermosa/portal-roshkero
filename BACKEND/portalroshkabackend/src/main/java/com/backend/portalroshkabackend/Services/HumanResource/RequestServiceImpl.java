package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.request.RequestResponseDto;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Repositories.TH.*;
import com.backend.portalroshkabackend.Repositories.TH.SolicitudRepository;
import com.backend.portalroshkabackend.Repositories.*;
import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.RequestMapper;
import com.backend.portalroshkabackend.tools.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backend.portalroshkabackend.tools.MessagesConst.*;

@Service("humanRequestService")
public class RequestServiceImpl implements IRequestService{
    private final SolicitudRepository solicitudRepository;
    private final RequestValidator requestValidator;
    private final RepositoryService repositoryService;

    @Autowired
    public RequestServiceImpl (SolicitudRepository solicitudRepository,
                               RequestValidator requestValidator,
                               RepositoryService repositoryService
    ){
        this.solicitudRepository = solicitudRepository;
        this.requestValidator = requestValidator;
        this.repositoryService = repositoryService;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SolicitudResponseDto> getByEstado(EstadoSolicitudEnum estado, Pageable pageable) {
        Page<Solicitud> requestSorted = solicitudRepository.findAllByEstado(estado, pageable);

        return requestSorted.map(RequestMapper::toSolicitudTHResponseDto);
    }

    @Transactional
    @Override
    public RequestResponseDto acceptRequest(int idRequest) {
        Solicitud request = repositoryService.findByIdOrThrow(
                solicitudRepository,
                idRequest,
                () -> new RequestNotFoundException(idRequest)
        );

        requestValidator.validateRequestStatus(request.getEstado(), request.getIdSolicitud());

        request.setEstado(EstadoSolicitudEnum.A);

        Solicitud acceptedRequest = repositoryService.save(
                solicitudRepository,
                request,
                DATABASE_DEFAULT_ERROR
        );

        return RequestMapper.toRequestResponseDto(acceptedRequest.getIdSolicitud(), REQUEST_ACCEPTED_MESSAGE);
    }

    @Transactional
    @Override
    public RequestResponseDto rejectRequest(int idRequest) {
        Solicitud request = repositoryService.findByIdOrThrow(
                solicitudRepository,
                idRequest,
                () -> new RequestNotFoundException(idRequest)
        );

        requestValidator.validateRequestStatus(request.getEstado(), request.getIdSolicitud());

        request.setEstado(EstadoSolicitudEnum.R); // Setea la solicitud como rechazada

        Solicitud rejectedRequest =  repositoryService.save(
                solicitudRepository,
                request,
                DATABASE_DEFAULT_ERROR
        );

        return RequestMapper.toRequestResponseDto(rejectedRequest.getIdSolicitud(), REQUEST_REJECTED_MESSAGE);

    }

}

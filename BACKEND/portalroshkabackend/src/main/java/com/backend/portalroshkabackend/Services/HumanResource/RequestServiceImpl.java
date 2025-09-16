package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.RequestRejectedDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Solicitudes;
import com.backend.portalroshkabackend.Repositories.RequestRepository;
import com.backend.portalroshkabackend.tools.SaveManager;
import com.backend.portalroshkabackend.tools.errors.errorslist.RequestNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.AutoMap;
import com.backend.portalroshkabackend.tools.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestServiceImpl implements IRequestService{
    private final RequestRepository requestRepository;

    private final Validator validator;

    @Autowired
    public RequestServiceImpl (RequestRepository requestRepository,
                              Validator validator){
        this.requestRepository = requestRepository;

        this.validator = validator;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RequestDto> getAllRequests(Pageable pageable) {
        Page<Solicitudes> requests = requestRepository.findAll(pageable);
        return requests.map(AutoMap::toRequestDto); // Retorna todas las solicitudes (DTOs)
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

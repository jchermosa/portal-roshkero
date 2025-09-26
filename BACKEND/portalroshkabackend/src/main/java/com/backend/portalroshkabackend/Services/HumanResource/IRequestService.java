package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.request.RequestResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRequestService {
    // Page<SolicitudTHResponseDto> getApprovedByLeader(Pageable pageable);

    Page<SolicitudResponseDto> getByTipoSolicitud(SolicitudesEnum tipoSolicitud, Pageable pageable);

    RequestResponseDto acceptRequest(int idRequest);
    RequestResponseDto rejectRequest(int idRequest);
    // RequestDto addNewRequestType();
}

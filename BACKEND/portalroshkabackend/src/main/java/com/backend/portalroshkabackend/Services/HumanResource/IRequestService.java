package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.self.RequestResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRequestService {
    // Page<SolicitudTHResponseDto> getApprovedByLeader(Pageable pageable);

    Page<SolicitudResponseDto> getByEstado(EstadoSolicitudEnum estado, Pageable pageable);

    RequestResponseDto acceptRequest(int idRequest);
    RequestResponseDto rejectRequest(int idRequest);
    // RequestDto addNewRequestType();
}

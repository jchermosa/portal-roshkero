package com.backend.portalroshkabackend.Services.Operations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.portalroshkabackend.DTO.Operationes.RequestResponseDto;

public interface IRequestService {
    Page<RequestResponseDto> getAllRequests(Pageable pageable);
}

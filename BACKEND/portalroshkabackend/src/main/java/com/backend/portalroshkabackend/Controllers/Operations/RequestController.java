/* 
package com.backend.portalroshkabackend.Controllers.Operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.DTO.Operationes.RequestResponseDto;
import com.backend.portalroshkabackend.Services.Operations.IRequestService;

@RestController
@RequestMapping("/api/v1/admin/operations/request")
public class RequestController {
    private final IRequestService requestService;

    @Autowired
    public RequestController(IRequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("")
    public ResponseEntity<Page<RequestResponseDto>> getAllRequests(
            @PageableDefault(size = 10, sort = "idSolicitud", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<RequestResponseDto> requests = requestService.getAllRequests(pageable);

        return ResponseEntity.ok(requests);
    }
}
*/
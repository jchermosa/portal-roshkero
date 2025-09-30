package com.backend.portalroshkabackend.Controllers.HumanResource;


import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.request.RequestResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
import com.backend.portalroshkabackend.Services.HumanResource.IRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin")
public class RequestController {
    private final IRequestService requestService;

    @Autowired
    public RequestController(IRequestService requestService){
        this.requestService = requestService;
    }

    @GetMapping("/th/users/requests/sortby")
    public ResponseEntity<Page<SolicitudResponseDto>> getBenefitsOrPermissions(
            @RequestParam(value = "type", required = true) String estado,
            @PageableDefault(size = 10, direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request
    ){
        Set<String> allowedParams = Set.of("type");

        for (String paramName : request.getParameterMap().keySet()){
            if (!allowedParams.contains(paramName)){
                throw new IllegalArgumentException("Parametro desconocido: " + paramName);
            }
        }

        if (estado.isBlank()) throw new IllegalArgumentException("El argumento del parametro no debe estar vacio");


        Page<SolicitudResponseDto> requests;

        switch (estado) {
            case "beneficio" -> requests = requestService.getBenefitsOrPermissions(SolicitudesEnum.BENEFICIO, pageable);
            case "permiso" -> requests = requestService.getBenefitsOrPermissions(SolicitudesEnum.PERMISO, pageable);
            default -> throw new IllegalArgumentException("Argumento del parametro invalido: " + estado);
        }

        return ResponseEntity.ok(requests);
    }

    @GetMapping("th/users/requests/vacations")
    public ResponseEntity<Page<SolicitudResponseDto>> getVacations(
            @PageableDefault(size = 10, direction = Sort.Direction.ASC) Pageable pageable
    ){
        Page<SolicitudResponseDto> requests = requestService.getVacations(SolicitudesEnum.VACACIONES, pageable);

        return ResponseEntity.ok(requests);
    }

    // TODO: getyByIdSolicitud?
    @GetMapping("/th/users/requests/{idRequest}")
    public ResponseEntity<SolicitudByIdResponseDto> getRequestById(
            @PathVariable int idRequest
    ){
        SolicitudByIdResponseDto request = requestService.getRequestById(idRequest);

        return ResponseEntity.ok(request);
    }

    @PostMapping("/th/users/requests/{idRequest}/accept")
    public ResponseEntity<RequestResponseDto> acceptRequest(@PathVariable int idRequest){

        RequestResponseDto response = requestService.acceptRequest(idRequest);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/th/users/requests/{idRequest}/reject")
    public ResponseEntity<RequestResponseDto> rejectRequest(@PathVariable int idRequest){

        RequestResponseDto response  = requestService.rejectRequest(idRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/th/users/requests")
    public ResponseEntity<?> addNewRequestType(){

        // TODO: Implementar cuando la base de datos tenga tipo de solicitudes
        return ResponseEntity.ok("agregar nuevo tipo de request") ;
    }


}

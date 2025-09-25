package com.backend.portalroshkabackend.Controllers.HumanResource;


import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.request.RequestResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
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
    public ResponseEntity<Page<SolicitudResponseDto>> getRequestSortByEstado(
            @RequestParam(value = "estado", required = true) String estado,
            @PageableDefault(size = 10, direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request
    ){
        Set<String> allowedParams = Set.of("estado");

        for (String paramName : request.getParameterMap().keySet()){
            if (!allowedParams.contains(paramName)){
                throw new IllegalArgumentException("Parametro desconocido: " + paramName);
            }
        }

        if (estado.isBlank()) throw new IllegalArgumentException("El argumento del parametro no debe estar vacio");


        Page<SolicitudResponseDto> requests;

        switch (estado) {
            case "A" -> requests = requestService.getByEstado(EstadoSolicitudEnum.A, pageable);
            case "R" -> requests = requestService.getByEstado(EstadoSolicitudEnum.R, pageable);
            case "P" -> requests = requestService.getByEstado(EstadoSolicitudEnum.P, pageable);
            default -> throw new IllegalArgumentException("Argumento del parametro invalido: " + estado);
        }

        return ResponseEntity.ok(requests);
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





    //TODO: dias disponibles{id}, dias totales de vacaciones, historial de solicitudes


    // ---------------------

    // mis solicitudes.GET
    // Listar por tipo de solicitud,
    // GET-Tipos de BENEFICIO, DISPOSITIVOS, PERMISOS. - LISTO
    // GET:Solicitudes aprobadas por lideres{PENDIENTES, FECHA} - LISTO
    // GET: listar por estado de la solicitud (A,I,P)
    // GET: Tipo de solicitudes

}

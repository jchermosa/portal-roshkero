package com.backend.portalroshkabackend.Controllers.HumanResource;

import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.RequestRejectedDto;
import com.backend.portalroshkabackend.DTO.th.*;
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

import java.util.List;
import java.util.Set;

// /th/users -> Cuando TH gestiona otros usuarios y sus solicitudes, etc
// /th/ -> cuanto TH necesita crear sus propias solicitudes


@RestController
@RequestMapping("/api/v1/admin")
public class RequestController {
    private final IRequestService requestService;

    @Autowired
    public RequestController(IRequestService requestService){
        this.requestService = requestService;
    }

    @GetMapping("/th/users/requests/leader/approved")
    public ResponseEntity<Page<SolicitudTHResponseDto>> getApprovedByLeader(
            @PageableDefault(size = 10, direction = Sort.Direction.ASC) Pageable pageable
    ){
        Page<SolicitudTHResponseDto> requests = requestService.getApprovedByLeader(pageable);

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/th/users/request")
    public ResponseEntity<Page<RequestDto>> getAllTHRequests(
            @PageableDefault(size = 10, sort = "idSolicitud", direction = Sort.Direction.ASC) Pageable pageable
    ){
        Page<RequestDto> requests = requestService.getAllRequests(pageable); // Solicitud Enviar -> Aprueba Lider/es -> Aparece en TH para aprobar / rechazar

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/th/users/request/sortby")
    public ResponseEntity<Page<SolicitudTHResponseDto>> getRequestSortByEstado(
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


        Page<SolicitudTHResponseDto> requests;

        switch (estado) {
            case "A" -> requests = requestService.getByEstado(EstadoSolicitudEnum.A, pageable);
            case "R" -> requests = requestService.getByEstado(EstadoSolicitudEnum.R, pageable);
            case "P" -> requests = requestService.getByEstado(EstadoSolicitudEnum.P, pageable);
            default -> throw new IllegalArgumentException("Argumento del parametro invalido: " + estado);
        }

        return ResponseEntity.ok(requests);
    }




    @PostMapping("/th/users/request/{id}/accept")
    public ResponseEntity<?> acceptRequest(@PathVariable int idRequest){

        boolean isAccepted = requestService.acceptRequest(idRequest);

        if (!isAccepted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/th/users/request/{id}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable int idRequest, @RequestBody RequestRejectedDto rejectedDto){

        boolean isRejected = requestService.rejectRequest(idRequest, rejectedDto);

        if (!isRejected){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/th/users/request")
    public ResponseEntity<?> addNewRequestType(){

        // TODO: Implementar cuando la base de datos tenga tipo de solicitudes
        return ResponseEntity.ok("agregar nuevo tipo de request") ;
    }





    //TODO: dias disponibles{id}, dias totales de vacaciones, historial de solicitudes

    //TODO: POST- Solicitar vaciones



    //TODO: POST-Solicitar BENEFICIO. - Cuando este la nueva DB

    // ---------------------

    // mis solicitudes.GET
    // Listar por tipo de solicitud,
    // GET-Tipos de BENEFICIO, DISPOSITIVOS, PERMISOS. - LISTO
    // GET:Solicitudes aprobadas por lideres{PENDIENTES, FECHA} - LISTO
    // GET: listar por estado de la solicitud (A,I,P)
    // GET: Tipo de solicitudes

}

package com.backend.portalroshkabackend.Controllers.HumanResource;

<<<<<<< HEAD
import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.RequestRejectedDto;
import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
=======

import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.request.RequestResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
>>>>>>> parent of dca61a3 (se elimino backend)
import com.backend.portalroshkabackend.Services.HumanResource.IRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
import java.util.List;
import java.util.Set;

// /th/users -> Cuando TH gestiona otros usuarios y sus solicitudes, etc
// /th/ -> cuanto TH necesita crear sus propias solicitudes


=======
import java.util.Set;

>>>>>>> parent of dca61a3 (se elimino backend)
@RestController
@RequestMapping("/api/v1/admin")
public class RequestController {
    private final IRequestService requestService;

    @Autowired
    public RequestController(IRequestService requestService){
        this.requestService = requestService;
    }

<<<<<<< HEAD
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
=======
    @GetMapping("/th/users/requests/sortby")
    public ResponseEntity<Page<SolicitudResponseDto>> getBenefitsOrPermissions(
            @RequestParam(value = "type", required = true) String estado,
            @PageableDefault(size = 10, direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request
    ){
        Set<String> allowedParams = Set.of("type");
>>>>>>> parent of dca61a3 (se elimino backend)

        for (String paramName : request.getParameterMap().keySet()){
            if (!allowedParams.contains(paramName)){
                throw new IllegalArgumentException("Parametro desconocido: " + paramName);
            }
        }

        if (estado.isBlank()) throw new IllegalArgumentException("El argumento del parametro no debe estar vacio");


<<<<<<< HEAD
        Page<SolicitudTHResponseDto> requests;

        switch (estado) {
            case "A" -> requests = requestService.getByEstado(EstadoSolicitudEnum.A, pageable);
            case "R" -> requests = requestService.getByEstado(EstadoSolicitudEnum.R, pageable);
            case "P" -> requests = requestService.getByEstado(EstadoSolicitudEnum.P, pageable);
=======
        Page<SolicitudResponseDto> requests;

        switch (estado) {
            case "beneficio" -> requests = requestService.getBenefitsOrPermissions(SolicitudesEnum.BENEFICIO, pageable);
            case "permiso" -> requests = requestService.getBenefitsOrPermissions(SolicitudesEnum.PERMISO, pageable);
>>>>>>> parent of dca61a3 (se elimino backend)
            default -> throw new IllegalArgumentException("Argumento del parametro invalido: " + estado);
        }

        return ResponseEntity.ok(requests);
    }

<<<<<<< HEAD



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
=======
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
>>>>>>> parent of dca61a3 (se elimino backend)
    public ResponseEntity<?> addNewRequestType(){

        // TODO: Implementar cuando la base de datos tenga tipo de solicitudes
        return ResponseEntity.ok("agregar nuevo tipo de request") ;
    }


<<<<<<< HEAD



    //TODO: dias disponibles{id}, dias totales de vacaciones, historial de solicitudes

    //TODO: POST- Solicitar vaciones

    // mis solicitudes.GET

    // Listar por tipo de solicitud,

    //TODO: POST-Solicitar BENEFICIO.

    // ---------------------

    // GET-Tipos de BENEFICIO, DISPOSITIVOS, PERMISOS. - LISTO
    // GET:Solicitudes aprobadas por lideres{PENDIENTES, FECHA} - LISTO
    // GET: listar por estado de la solicitud (A,I,P)
    // GET: Tipo de solicitudes

=======
>>>>>>> parent of dca61a3 (se elimino backend)
}

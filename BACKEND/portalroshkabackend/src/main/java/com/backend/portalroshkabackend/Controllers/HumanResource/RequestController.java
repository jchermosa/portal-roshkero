package com.backend.portalroshkabackend.Controllers.HumanResource;

import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.RequestRejectedDto;
import com.backend.portalroshkabackend.Services.HumanResource.IRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.ToDoubleBiFunction;

@RestController
@RequestMapping("/api/v1/admin")
public class RequestController {
    private final IRequestService requestService;

    @Autowired
    public RequestController(IRequestService requestService){
        this.requestService = requestService;
    }

    @GetMapping("/th/users/request")
    public ResponseEntity<Page<RequestDto>> getAllRequests(
            @PageableDefault(size = 10, sort = "idSolicitud", direction = Sort.Direction.ASC) Pageable pageable
    ){
        Page<RequestDto> requests = requestService.getAllRequests(pageable);

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

    //TODO: Tipo de solicitudes, GET:Solicitudes aprobadas por lideres{
    //  PENDIENTES, FECHA}


    //TODO: dias disponibles{id}, dias totales de vacaciones, historial de solicitudes

    //TODO: POST- Solicitar vaciones

    //TODO: GET-LIDERES ASIGNADOS AL USUARIO(active)

    //TODO; mis solicitudes.GET

    //TODO: Listar por tipo de solicitud, listar por estado de la solicitud (A,I,P)
    //TODO: POST-Solicitar BENEFICIO.

    //TODO: GET-Tipos de BENEFICIO, DISPOSITIVOS, PERMISOS.



}

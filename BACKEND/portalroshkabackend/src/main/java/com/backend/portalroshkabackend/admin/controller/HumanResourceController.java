package com.backend.portalroshkabackend.admin.controller;

import com.backend.portalroshkabackend.admin.dto.*;
import com.backend.portalroshkabackend.admin.service.IHumanResourceService;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class HumanResourceController {
    private final IHumanResourceService humanResourceService;

    @Autowired
    public HumanResourceController(IHumanResourceService humanResourceService){
        this.humanResourceService = humanResourceService;
    }

    // ----------------- Users -----------------
    @GetMapping("/th/users")
    public ResponseEntity<Page<UserDto>> getAllEmployees(
            @PageableDefault(size = 10, sort = "idUsuario", direction = Sort.Direction.ASC)Pageable pageable
    ){
        Page<UserDto> users = humanResourceService.getAllEmployees(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/th/users/active")
    public ResponseEntity<Page<UserDto>> getAllActiveEmployess(
            @PageableDefault(size = 10, sort = "id_usuario", direction = Sort.Direction.ASC)Pageable pageable // Dejar en 'id_usuario' ya que es una consulta creada manualmente en UserRepository, de forma nativa
    ){
        Page<UserDto> users = humanResourceService.getAllActiveEmployees(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/th/users/inactive")
    public ResponseEntity<Page<UserDto>> getAllInactiveEmployees(
            @PageableDefault(size = 10, sort = "id_usuario", direction = Sort.Direction.ASC)Pageable pageable // Lo mismo con este, dejar en 'id_usuario' independientemente de como este en el modelo Usuario
    ){
        Page<UserDto> users = humanResourceService.getAllInactiveEmployees(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/th/users/{id}")
    public ResponseEntity<UserDto> getEmployeeById(@PathVariable int id){
        UserDto user = humanResourceService.getEmployeeById(id);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/th/users")
    public ResponseEntity<UserDto> addEmployee(@RequestBody UserInsertDto insertDto){
        UserDto user = humanResourceService.addEmployee(insertDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/th/users/{id}")
                .buildAndExpand(user.getIdUsuario())
                .toUri();
        return ResponseEntity.created(location).body(user);
    }

    @PutMapping("/th/users/{id}")
    public ResponseEntity<UserDto> updateEmployee(@RequestBody UserUpdateDto updateDto, @PathVariable int id){
        UserDto user = humanResourceService.updateEmployee(updateDto, id);

        if (user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/th/users/{id}")
    public ResponseEntity<UserDto> deleteEmployee(@PathVariable int id){
        UserDto user = humanResourceService.deleteEmployee(id);

        if (user == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    // ----------------- Request -----------------
    @GetMapping("/th/users/request")
    public ResponseEntity<Page<RequestDto>> getAllRequests(
            @PageableDefault(size = 10, sort = "idSolicitud", direction = Sort.Direction.ASC)Pageable pageable
    ){
        Page<RequestDto> requests = humanResourceService.getAllRequests(pageable);

        return ResponseEntity.ok(requests);
    }

    @PostMapping("/th/users/request/{id}/accept")
    public ResponseEntity<?> acceptRequest(@PathVariable int idRequest){

        boolean isAccepted = humanResourceService.acceptRequest(idRequest);

        if (!isAccepted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/th/users/request/{id}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable int idRequest, @RequestBody RequestRejectedDto rejectedDto){

        boolean isRejected = humanResourceService.rejectRequest(idRequest, rejectedDto);

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
}

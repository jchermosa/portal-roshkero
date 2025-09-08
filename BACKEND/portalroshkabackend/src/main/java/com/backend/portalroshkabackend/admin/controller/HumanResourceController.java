package com.backend.portalroshkabackend.admin.controller;

import com.backend.portalroshkabackend.admin.dto.UserDto;
import com.backend.portalroshkabackend.admin.dto.UserInsertDto;
import com.backend.portalroshkabackend.admin.dto.UserUpdateDto;
import com.backend.portalroshkabackend.admin.service.IHumanResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class HumanResourceController {
    private final IHumanResourceService humanResourceService;

    @Autowired
    public HumanResourceController(IHumanResourceService humanResourceService){
        this.humanResourceService = humanResourceService;
    }

    // Users
    @GetMapping("/th/users")
    public ResponseEntity<List<UserDto>> getAllEmployees(){
        List<UserDto> users = humanResourceService.getAllEmployees();

        return ResponseEntity.ok(users);
    }

    @PostMapping("/th/users")
    public ResponseEntity<UserDto> addEmployee(UserInsertDto insertDto){
        UserDto user = humanResourceService.addEmployee(insertDto);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/th/users/{id}")
    public ResponseEntity<UserDto> updateEmployee(UserUpdateDto updateDto, int id){
        UserDto user = humanResourceService.updateEmployee(updateDto, id);

        return ResponseEntity.ok(user) ;
    }

    @DeleteMapping("/th/users/{id}")
    public ResponseEntity<UserDto> deleteEmployee(int id){
        UserDto user = humanResourceService.deleteEmployee(id);

        return ResponseEntity.ok(user);
    }

    // Request
    @GetMapping("/th/users/request")
    public ResponseEntity<String> getAllRequests(){
        return ResponseEntity.ok("retornando todos las solicitudes") ;
    }

    @PostMapping("/th/users/request/{id}")
    public ResponseEntity<String> manageRequest(){
        return ResponseEntity.ok("aceptar/rechazar solicitud") ;
    }

    @PostMapping("/th/users/request")
    public ResponseEntity<String> addNewRequestType(){


        return ResponseEntity.ok("agregar nuevo tipo de request") ;
    }
}

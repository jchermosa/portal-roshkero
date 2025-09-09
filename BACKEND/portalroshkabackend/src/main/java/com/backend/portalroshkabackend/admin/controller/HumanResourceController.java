package com.backend.portalroshkabackend.admin.controller;

import com.backend.portalroshkabackend.admin.dto.UserDto;
import com.backend.portalroshkabackend.admin.dto.UserInsertDto;
import com.backend.portalroshkabackend.admin.dto.UserUpdateDto;
import com.backend.portalroshkabackend.admin.service.IHumanResourceService;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<UserDto>> getAllEmployees(){
        List<UserDto> users = humanResourceService.getAllEmployees();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/th/users/active")
    public ResponseEntity<List<UserDto>> getAllActiveEmployess(){
        List<UserDto> users = humanResourceService.getAllActiveEmployees();

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

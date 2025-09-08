package com.backend.portalroshkabackend.admin.th.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class HumanResourceController {

    // Users
    @GetMapping("/th/users")
    public String getAllEmployees(){
        return "retornando todos los usuarios";
    }

    @PostMapping("/th/users/{id}")
    public String updateEmployee(int id){
        return "actualizando usuario";
    }

    @DeleteMapping("/th/users/{id}")
    public String deleteEmployee(int id){
        return "elíminando usuario";
    }

    // Request
    @GetMapping("/th/users/request")
    public String getAllRequests(){
        return "retornando todos las solicitudes";
    }

    @PostMapping("/th/users/request/{id}")
    public String manageRequest(){
        return "aceptar/rechazar solicitud";
    }

    @PostMapping("/th/users/request")
    public String addNewRequestType(){
        return "agregar nuevo tipo de request";
    }
}

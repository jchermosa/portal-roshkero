// package com.backend.portalroshkabackend.admin.controller;

// import com.backend.portalroshkabackend.admin.service.IHumanResourceService;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/v1/admin")
// public class HumanResourceController {
//     private final IHumanResourceService humanResourceService;

//     public HumanResourceController(IHumanResourceService humanResourceService){
//         this.humanResourceService = humanResourceService;
//     }

//     // Users
//     @GetMapping("/th/users")
//     public ResponseEntity<String> getAllEmployees(){


//         return ResponseEntity.ok("retornando todos los usuarios");
//     }

//     @PostMapping("/th/users/{id}")
//     public ResponseEntity<String> updateEmployee(int id){
//         return ResponseEntity.ok("actualizando usuario") ;
//     }

//     @DeleteMapping("/th/users/{id}")
//     public ResponseEntity<String> deleteEmployee(int id){
//         return ResponseEntity.ok("elíminando usuario") ;
//     }

//     // Request
//     @GetMapping("/th/users/request")
//     public ResponseEntity<String> getAllRequests(){
//         return ResponseEntity.ok("retornando todos las solicitudes") ;
//     }

//     @PostMapping("/th/users/request/{id}")
//     public ResponseEntity<String> manageRequest(){
//         return ResponseEntity.ok("aceptar/rechazar solicitud") ;
//     }

//     @PostMapping("/th/users/request")
//     public ResponseEntity<String> addNewRequestType(){


//         return ResponseEntity.ok("agregar nuevo tipo de request") ;
//     }
// }

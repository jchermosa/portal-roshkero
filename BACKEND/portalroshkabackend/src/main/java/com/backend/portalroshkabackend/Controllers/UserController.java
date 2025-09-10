package com.backend.portalroshkabackend.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.portalroshkabackend.Models.Usuarios;
import com.backend.portalroshkabackend.Services.UserService;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/user")
    public Usuarios getUsers() {
        System.out.println("ENTRANDO AL CONTROLLER"); 
        Usuarios user = userService.getUserByCorreo("juan.perez@example.com");

        return user;
    }


    @GetMapping("/v1/admin/operaciones")
    public String getOperations() {
        return "Operaciones administrativas realizadas con éxito."; 
    }

    @GetMapping("/v1/admin/th")
    public String getTh() {
        return " Consulta de recursos humanos realizadas con éxito."; 
    }
    
    
}



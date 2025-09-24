package com.backend.portalroshkabackend.Controllers.UsuarioController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.portalroshkabackend.DTO.UsuarioDTO.SolicitudUserDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserHomeDto;
// import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserSolPermisoDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserUpdateDto;
import com.backend.portalroshkabackend.Services.UsuarioServicio.UserService;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuariosController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<UserHomeDto> getUsuarioHome() {
        UserHomeDto user = userService.getUsuarioHome();
        System.out.println("\n \n Usuario DTO: \n\n" + user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUsuarioActual() {
        UserDto user = userService.getUsuarioActual();
        System.out.println("\n \n Usuario DTO: \n\n" + user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserUpdateDto> updateUsuarioActual(@RequestBody UserUpdateDto updateDto) {
        UserUpdateDto user = userService.updateUsuarioActual(updateDto);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/solicitudes") // Endpoint para obtener las solicitudes del usuario actual 
    public ResponseEntity<List<SolicitudUserDto>> getSolicitudesUsuarioActual() {
        List<SolicitudUserDto> solicitudes = userService.getSolicitudesUsuarioActual();
        return ResponseEntity.ok(solicitudes);
    }

    // @PostMapping("/crear_permiso") // Endpoint para crear una nueva solicitud para el usuario actual
    // public ResponseEntity<String> crearPermisoUsuarioActual(@RequestBody UserSolPermisoDto solPermisoDto) {
    //     userService.crearPermisoUsuarioActual(solPermisoDto);
    //     return ResponseEntity.status(201).body("Solicitud creada con éxito");
    // }

    // Endpoints comentados para futuras implementaciones
    // @GetMapping("/vacaciones")
    // public ResponseEntity<VacacionesDto> getVacacionesUsuarioActual() {
    //     VacacionesDto vacaciones = userService.getVacacionesUsuarioActual();
    //     return ResponseEntity.ok(vacaciones);
    // }

    // @PostMapping("/solicitudes")
    // public ResponseEntity<String> crearSolicitud(@RequestBody SolicitudDto solicitudDto) {
    //     userService.crearSolicitudUsuarioActual(solicitudDto);
    //     return ResponseEntity.status(HttpStatus.CREATED).body("Solicitud creada con éxito");
    // }
}
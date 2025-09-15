package com.backend.portalroshkabackend.Controllers;

/*
*   EN ESTA CLASE SE MANEJAN TODOS LOS ENDPOINTS QUE PUEDE
*   EJECUTAR UN USUARIO DENTRO DE SU ALCANCE
*
*   VER INFORMACION PERSONAL
*   VER SOLICITUDES PROPIAS
*   VER VACACINES
*   CREAR SOLICITUD
*
* */

import com.backend.portalroshkabackend.Services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backend.portalroshkabackend.DTO.UserDto;
// import com.backend.portalroshkabackend.DTO.SolicitudDto;
// import com.backend.portalroshkabackend.DTO.VacacionesDto;
import com.backend.portalroshkabackend.DTO.UserUpdateDto;

import java.util.List;



@RestController
@RequestMapping("/api/v1/usuarios")

public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUsuarioActual() {
        UserDto user = usuariosService.getUsuarioActual();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateUsuarioActual(@RequestBody UserUpdateDto updateDto) {
        UserDto user = usuariosService.updateUsuarioActual(updateDto);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // @GetMapping("/solicitudes")
    // public ResponseEntity<List<SolicitudDto>> getSolicitudesUsuarioActual() {
    //     List<SolicitudDto> solicitudes = usuariosService.getSolicitudesUsuarioActual();
    //     return ResponseEntity.ok(solicitudes);
    // }

    // @GetMapping("/vacaciones")
    // public ResponseEntity<VacacionesDto> getVacacionesUsuarioActual() {
    //     VacacionesDto vacaciones = usuariosService.getVacacionesUsuarioActual();
    //     return ResponseEntity.ok(vacaciones);
    // }

    // @PostMapping("/solicitudes")
    // public ResponseEntity<String> crearSolicitud(@RequestBody SolicitudDto solicitudDto) {
    //     usuariosService.crearSolicitudUsuarioActual(solicitudDto);
    //     return ResponseEntity.status(HttpStatus.CREATED).body("Solicitud creada con éxito");
    // }


}

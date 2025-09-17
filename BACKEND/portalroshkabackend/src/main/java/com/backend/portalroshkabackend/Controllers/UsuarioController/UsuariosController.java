package com.backend.portalroshkabackend.Controllers.UsuarioController;

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

import com.backend.portalroshkabackend.Services.UsuariosService.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserHomeDto;
// import com.backend.portalroshkabackend.DTO.SolicitudDto;
// import com.backend.portalroshkabackend.DTO.VacacionesDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserUpdateDto;


@RestController
@RequestMapping("/api/v1/usuarios")

public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    @GetMapping("/")
    public ResponseEntity<UserHomeDto> getUsuarioHome() {
        UserHomeDto user = usuariosService.getUsuarioHome();

        System.out.println("\n \n Usuario DTO: \n\n" + user); // imprimir el usuario en la consola

        return ResponseEntity.ok(user);
    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> getUsuarioActual() {
        UserDto user = usuariosService.getUsuarioActual();

        System.out.println("\n \n Usuario DTO: \n\n" + user); // imprimir el usuario en la consola

        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserUpdateDto> updateUsuarioActual(@RequestBody UserUpdateDto updateDto) {
        UserUpdateDto user = usuariosService.updateUsuarioActual(updateDto);

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

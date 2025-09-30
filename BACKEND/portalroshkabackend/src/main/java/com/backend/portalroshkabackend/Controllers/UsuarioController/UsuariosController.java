package com.backend.portalroshkabackend.Controllers.UsuarioController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.portalroshkabackend.DTO.UsuarioDTO.SolicitudUserDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserCambContrasDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserHomeDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserSolBeneficioDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserSolDispositivoDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserSolPermisoDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserSolVacacionDto;
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
        System.out.println("\n \n getUsuarioHome: \n\n" + user + "\n \n UserHomeDto \n\n");
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

    @PostMapping("/crear_permiso") // Endpoint para solcitar un Permiso para el usuario actual
    public ResponseEntity<UserSolPermisoDto> crearPermisoUsuarioActual(@RequestBody UserSolPermisoDto solPermisoDto) {
        System.out.println("\n \n Solicitud DTO Recibida en el Controller: \n\n" + solPermisoDto + "\n \n");
        solPermisoDto = userService.crearPermisoUsuarioActual(solPermisoDto);
        return ResponseEntity.ok(solPermisoDto);
    }

    @PostMapping("/crear_beneficio") // Endpoint para solicitar un Beneficio para el usuario actual
    public ResponseEntity<UserSolBeneficioDto> crearBeneficioUsuarioActual(@RequestBody UserSolBeneficioDto solBeneficioDto) {
        // process POST request
        solBeneficioDto = userService.crearBeneficioUsuarioActual(solBeneficioDto);
        return ResponseEntity.ok(solBeneficioDto);
    }

    @PostMapping("/crear_vacacion") // Endpoint para solicitar Vacaciones para el usuario actual
    public ResponseEntity<UserSolVacacionDto> crearVacacionUsuarioActual(@RequestBody UserSolVacacionDto solVacacionDto) {
        //TODO: process POST request
        solVacacionDto = userService.crearVacacionUsuarioActual(solVacacionDto);
        return ResponseEntity.ok(solVacacionDto);
    }
    
    @PostMapping("/pedir_dispositivo")
    public ResponseEntity<UserSolDispositivoDto> pedirDispositivoUsuarioActual(@RequestBody UserSolDispositivoDto solDispositivoDto) {
        //TODO: process POST request
        solDispositivoDto = userService.pedirDispositivoUsuarioActual(solDispositivoDto);
        return ResponseEntity.ok(solDispositivoDto);
    }
    
    @PostMapping("/cambiar_contrasena")
    public ResponseEntity<?> cambiarContrasena(@RequestBody UserCambContrasDto dto) {
        boolean actualizado = userService.actualizarContrasena(dto);

        if (actualizado) {
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("La contraseña actual es incorrecta");
        }
    }

}
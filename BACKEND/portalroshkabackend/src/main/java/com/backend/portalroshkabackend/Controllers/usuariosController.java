package com.backend.portalroshkabackend.Controllers;


import com.backend.portalroshkabackend.Models.Usuarios;
import com.backend.portalroshkabackend.Repositories.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backend.portalroshkabackend.Services.UsuariosService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/usuarios")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    UsuariosRepository usuariosRepo;


    @GetMapping
    public List<Usuarios> getAll() {
        return usuariosService.getUsuarios();
    }

    @GetMapping("/{id_usuario}")
    public Optional<Usuarios> getById(@PathVariable("id_usuario") Integer ID) {
        return usuariosService.getUsuarios(ID);
    }

    @PostMapping
    public ResponseEntity<String> saveUsuario(@RequestBody Usuarios User) {

        // 1. Verificar que el usuario con ese ID existe
        if (User.getIdUsuario() != null) {
            Optional<Usuarios> existenteID = usuariosRepo.findById(User.getIdUsuario());
            if (existenteID.isPresent()) {
                System.out.println("el usuario con esa ID YA existe");
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Ya existe un usuario con esa ID: " + User.getIdUsuario() + " SU CI es " + existenteID.get().getNroCedula());
            }
        }

        // Verificar duplicado por nroCedula
        Optional<Usuarios> existente = usuariosRepo.findByNroCedula(User.getNroCedula());
        if (existente.isPresent()) {
            System.out.println("Ya existe un usuario con el número de cédula: " + User.getNroCedula());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Ya existe un usuario con el número de cédula: " + User.getNroCedula());
        }

        if (User.getFechaIngreso() != null) {
            Period period = Period.between(User.getFechaIngreso(), LocalDate.now());
            String antiguedad = String.format("%d años, %d meses, %d días",
                    period.getYears(), period.getMonths(), period.getDays());
            User.setAntiguedad(antiguedad);
        }
        usuariosService.saveUsuarios(User);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado con éxito");
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody Usuarios user) {

        // 1. Verificar que el usuario con ese ID existe
        Optional<Usuarios> existente = usuariosRepo.findById(id);
        if (existente.isEmpty()) {
            System.out.println("No se encontró un usuario con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró un usuario con ID: " + id);
        }

        // 2. Comprobar que el nroCedula no esté usado por otro usuario
        Optional<Usuarios> cedulaDuplicada = usuariosRepo.findByNroCedula(user.getNroCedula());
        if (cedulaDuplicada.isPresent() && !cedulaDuplicada.get().getIdUsuario().equals(id)) {
            System.out.println("Ya existe un usuario con el número de cédula: " + user.getNroCedula());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un usuario con el número de cédula: " + user.getNroCedula());
        }

        // 3. Mantener el mismo ID
        user.setIdUsuario(id);

        // 4. Calcular antigüedad si corresponde
        if (user.getFechaIngreso() != null) {
            Period period = Period.between(user.getFechaIngreso(), LocalDate.now());
            String antiguedad = String.format("%d años, %d meses, %d días",
                    period.getYears(), period.getMonths(), period.getDays());
            user.setAntiguedad(antiguedad);
        }

        // 5. Guardar cambios
        usuariosService.saveUsuarios(user);

        return ResponseEntity.ok("Usuario actualizado con éxito");
    }

    @DeleteMapping("/{id_usuario}")
    public void saveUpdate(@PathVariable("id_usuario") Integer ID) {
        usuariosService.delete(ID);
    }


}

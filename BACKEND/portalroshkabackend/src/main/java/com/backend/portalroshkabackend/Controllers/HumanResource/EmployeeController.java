package com.backend.portalroshkabackend.Controllers.HumanResource;

import com.backend.portalroshkabackend.DTO.*;
<<<<<<< HEAD
import com.backend.portalroshkabackend.DTO.th.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.UserResponseDto;
import com.backend.portalroshkabackend.Services.HumanResource.IEmployeeService;
import com.backend.portalroshkabackend.Services.HumanResource.IThSelfService;
=======
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.DTO.th.employees.DefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Services.HumanResource.IEmployeeService;
>>>>>>> parent of dca61a3 (se elimino backend)
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

<<<<<<< HEAD
=======

>>>>>>> parent of dca61a3 (se elimino backend)
@RestController
@RequestMapping("/api/v1/admin")
public class EmployeeController {
    private final IEmployeeService employeeService;

     @Autowired
<<<<<<< HEAD
    public EmployeeController(IEmployeeService employeeService,
                              IThSelfService thService){
=======
    public EmployeeController(IEmployeeService employeeService){
>>>>>>> parent of dca61a3 (se elimino backend)
         this.employeeService = employeeService;
     }

    // ----------------- Users -----------------
<<<<<<< HEAD
    @GetMapping("/th/users")
    public ResponseEntity<?> getAllEmployees(
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @PageableDefault(size = 10, sort = "idUsuario", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request
    ){
        Set<String> allowedParams = Set.of("sortBy");
=======
    @PostMapping("/th/users/{id}/resetpassword")
    public ResponseEntity<DefaultResponseDto> resetUserPassword(
            @PathVariable int id
    ){
        DefaultResponseDto response = employeeService.resetUserPassword(id);

        return ResponseEntity.ok(response);
    }

// TODO traer solo la informacion necesaria
    @GetMapping("/th/users")
    public ResponseEntity<?> getAllEmployees(
            @RequestParam(value = "rol_id", required = false) Integer rolId,
            @RequestParam(value = "cargo_id", required = false) Integer cargoId,
            @RequestParam(value = "estado", required = false) String estadoStr,
            @PageableDefault(size = 10, sort = "idUsuario", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request
    ){
        Set<String> allowedParams = Set.of("rol_id", "cargo_id", "estado", "page", "size");
>>>>>>> parent of dca61a3 (se elimino backend)

        for (String paramName : request.getParameterMap().keySet()){
            if (!allowedParams.contains(paramName)){
                throw new IllegalArgumentException("Parametro desconocido: " + paramName);
            }
        }

<<<<<<< HEAD
        if (sortBy == null) return ResponseEntity.ok(employeeService.getAllEmployees(pageable));

        if (sortBy.isBlank()) throw new IllegalArgumentException("El argumento del parametro no debe estar vacio");

        Page<UserResponseDto> users;

         switch (sortBy) {
            case "active" -> users = employeeService.getAllActiveEmployees(pageable);
            case "inactive" -> users = employeeService.getAllInactiveEmployees(pageable);
            case "rol" -> users = employeeService.getAllEmployeesByRol(pageable);
            case "cargo" -> users = employeeService.getAllEmployeesByPosition(pageable);
            default -> throw new IllegalArgumentException("Argumento del parametro invalido: " + sortBy);
        }

        return ResponseEntity.ok(users);
    }

=======
        EstadoActivoInactivo estado = null;
        if (estadoStr != null) {
            try {
                estado = EstadoActivoInactivo.valueOf(estadoStr);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de 'estado' inválido: " + estadoStr);
            }
        }

        Page<UserResponseDto> users = employeeService.getAllEmployeesByFilters(rolId, cargoId, estado, pageable);


        return ResponseEntity.ok(users);
    }

    //TODO: Obtener todos los datos de un usuario (y su foto de perfil y tecnologias), tambien el historial de equipos en los que estuvo.

>>>>>>> parent of dca61a3 (se elimino backend)
    @GetMapping("/th/users/{id}")
    public ResponseEntity<UserByIdResponseDto> getEmployeeById(@PathVariable int id){
        UserByIdResponseDto user = employeeService.getEmployeeById(id);

        return ResponseEntity.ok(user);
    }

<<<<<<< HEAD
    @PostMapping("/th/users")
    public ResponseEntity<UserResponseDto> addEmployee(@RequestBody UserInsertDto insertDto){
        UserResponseDto user = employeeService.addEmployee(insertDto);
=======
    @GetMapping("/th/users/cedula/{cedula}")
    public ResponseEntity<UserDto> getEmployeeByCedula(@PathVariable String cedula){
        UserDto userDto = employeeService.getEmployeeByCedula(cedula);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/th/users")
    public ResponseEntity<DefaultResponseDto> addEmployee(@RequestBody UserInsertDto insertDto){
        DefaultResponseDto user = employeeService.addEmployee(insertDto);
>>>>>>> parent of dca61a3 (se elimino backend)

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/th/users/{id}")
                .buildAndExpand(user.getIdUsuario())
                .toUri(); // Para retornar en el header la ubicacion en donde se puede encontrar el usuario creado, por si el frontend lo necesita
        return ResponseEntity.created(location).body(user);
    }

    @PutMapping("/th/users/{id}")
<<<<<<< HEAD
    public ResponseEntity<UserResponseDto> updateEmployee(@RequestBody UserUpdateDto updateDto, @PathVariable int id){
        UserResponseDto user = employeeService.updateEmployee(updateDto, id);
=======
    public ResponseEntity<DefaultResponseDto> updateEmployee(@RequestBody UserUpdateDto updateDto, @PathVariable int id){
        DefaultResponseDto user = employeeService.updateEmployee(updateDto, id);
>>>>>>> parent of dca61a3 (se elimino backend)

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/th/users/{id}")
<<<<<<< HEAD
    public ResponseEntity<UserResponseDto> deleteEmployee(@PathVariable int id){
        UserResponseDto user = employeeService.deleteEmployee(id);
=======
    public ResponseEntity<DefaultResponseDto> deleteEmployee(@PathVariable int id){
        DefaultResponseDto user = employeeService.deleteEmployee(id);
>>>>>>> parent of dca61a3 (se elimino backend)

        return ResponseEntity.ok(user);
    }

}

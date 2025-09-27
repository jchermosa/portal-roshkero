package com.backend.portalroshkabackend.Controllers.HumanResource;

import com.backend.portalroshkabackend.DTO.*;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.DTO.th.employees.DefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserResponseDto;
import com.backend.portalroshkabackend.Services.HumanResource.IEmployeeService;
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


@RestController
@RequestMapping("/api/v1/admin")
public class EmployeeController {
    private final IEmployeeService employeeService;

     @Autowired
    public EmployeeController(IEmployeeService employeeService){
         this.employeeService = employeeService;
     }

    // ----------------- Users -----------------
    @PostMapping("/th/users/{id}/resetpassword")
    public ResponseEntity<DefaultResponseDto> resetUserPassword(
            @PathVariable int id
    ){
        DefaultResponseDto response = employeeService.resetUserPassword(id);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/th/users")
    public ResponseEntity<?> getAllEmployees(
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @PageableDefault(size = 10, sort = "idUsuario", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request
    ){
        Set<String> allowedParams = Set.of("sortBy", "page", "size");

        for (String paramName : request.getParameterMap().keySet()){
            if (!allowedParams.contains(paramName)){
                throw new IllegalArgumentException("Parametro desconocido: " + paramName);
            }
        }

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

    //TODO: Obtener todos los datos de un usuario (y su foto de perfil y tecnologias), tambien el historial de equipos en los que estuvo.

    @GetMapping("/th/users/{id}")
    public ResponseEntity<UserByIdResponseDto> getEmployeeById(@PathVariable int id){
        UserByIdResponseDto user = employeeService.getEmployeeById(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/th/users/cedula/{cedula}")
    public ResponseEntity<UserDto> getEmployeeByCedula(@PathVariable String cedula){
        UserDto userDto = employeeService.getEmployeeByCedula(cedula);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/th/users")
    public ResponseEntity<DefaultResponseDto> addEmployee(@RequestBody UserInsertDto insertDto){
        DefaultResponseDto user = employeeService.addEmployee(insertDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/th/users/{id}")
                .buildAndExpand(user.getIdUsuario())
                .toUri(); // Para retornar en el header la ubicacion en donde se puede encontrar el usuario creado, por si el frontend lo necesita
        return ResponseEntity.created(location).body(user);
    }

    @PutMapping("/th/users/{id}")
    public ResponseEntity<DefaultResponseDto> updateEmployee(@RequestBody UserUpdateDto updateDto, @PathVariable int id){
        DefaultResponseDto user = employeeService.updateEmployee(updateDto, id);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/th/users/{id}")
    public ResponseEntity<DefaultResponseDto> deleteEmployee(@PathVariable int id){
        DefaultResponseDto user = employeeService.deleteEmployee(id);

        return ResponseEntity.ok(user);
    }

}

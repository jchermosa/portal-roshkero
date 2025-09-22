package com.backend.portalroshkabackend.Controllers.HumanResource;

import com.backend.portalroshkabackend.DTO.*;
import com.backend.portalroshkabackend.DTO.th.employees.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.DefaultResponseDto;
import com.backend.portalroshkabackend.Services.HumanResource.IEmployeeService;
import com.backend.portalroshkabackend.Services.HumanResource.IThSelfService;
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
    public EmployeeController(IEmployeeService employeeService,
                              IThSelfService thService){
         this.employeeService = employeeService;
     }

    // ----------------- Users -----------------
    @GetMapping("/th/users")
    public ResponseEntity<?> getAllEmployees(
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @PageableDefault(size = 10, sort = "idUsuario", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request
    ){
        Set<String> allowedParams = Set.of("sortBy");

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

    @GetMapping("/th/users/{id}")
    public ResponseEntity<UserByIdResponseDto> getEmployeeById(@PathVariable int id){
        UserByIdResponseDto user = employeeService.getEmployeeById(id);

        return ResponseEntity.ok(user);
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

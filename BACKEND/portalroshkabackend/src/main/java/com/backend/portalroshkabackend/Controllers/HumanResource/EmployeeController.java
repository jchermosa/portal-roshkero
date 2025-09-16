package com.backend.portalroshkabackend.Controllers.HumanResource;

import com.backend.portalroshkabackend.DTO.*;
import com.backend.portalroshkabackend.Services.HumanResource.IEmployeeService;
import com.backend.portalroshkabackend.Services.HumanResource.ITHService;
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

@RestController("employeeController")
@RequestMapping("/api/v1/admin")
public class EmployeeController {
    private final IEmployeeService employeeService;
    private final ITHService thService;

     @Autowired
    public EmployeeController(IEmployeeService employeeService,
                              ITHService thService){
         this.employeeService = employeeService;
         this.thService = thService;
     }

    // ----------------- Me -----------------
    @PutMapping("th/me/update/{id}")
    public ResponseEntity<?> updateMe(
            @RequestParam(value = "newEmail", required = false) String newEmail,
            @RequestParam(value = "newPhone", required = false) String newPhone,
            @PathVariable int id
    )
    {
        if (newEmail != null){
            EmailUpdatedDto dto = thService.updateEmail(id, newEmail);
            return ResponseEntity.ok(dto);
        } else if (newPhone != null) {
            PhoneUpdatedDto dto = thService.updatePhone(id, newPhone);
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.badRequest().build();
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

        Page<UserDto> users;

         switch (sortBy) {
            case "active" -> users = employeeService.getAllActiveEmployees(pageable);
            case "inactive" -> users = employeeService.getAllInactiveEmployees(pageable);
            case "rol" -> users = employeeService.getAllEmployeesByRol(pageable);
            // case "equipo" -> users = employeeService.getAllEmployeesByTeam(pageable);
            case "cargo" -> users = employeeService.getAllEmployeesByPosition(pageable);
            default -> throw new IllegalArgumentException("Argumento del parametro invalido: " + sortBy);
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/th/users/{id}")
    public ResponseEntity<UserDto> getEmployeeById(@PathVariable int id){
        UserDto user = employeeService.getEmployeeById(id);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/th/users")
    public ResponseEntity<UserDto> addEmployee(@RequestBody UserInsertDto insertDto){
        UserDto user = employeeService.addEmployee(insertDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/th/users/{id}")
                .buildAndExpand(user.getIdUsuario())
                .toUri(); // Para retornar en el header la ubicacion en donde se puede encontrar el usuario creado, por si el frontend lo necesita
        return ResponseEntity.created(location).body(user);
    }

    @PutMapping("/th/users/{id}")
    public ResponseEntity<UserDto> updateEmployee(@RequestBody UserUpdateDto updateDto, @PathVariable int id){
        UserDto user = employeeService.updateEmployee(updateDto, id);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/th/users/{id}")
    public ResponseEntity<UserDto> deleteEmployee(@PathVariable int id){
        UserDto user = employeeService.deleteEmployee(id);

        return ResponseEntity.ok(user);
    }

}

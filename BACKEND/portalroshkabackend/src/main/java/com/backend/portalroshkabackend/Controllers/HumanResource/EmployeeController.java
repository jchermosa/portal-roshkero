package com.backend.portalroshkabackend.Controllers.HumanResource;

import com.backend.portalroshkabackend.DTO.*;
import com.backend.portalroshkabackend.Services.HumanResource.IEmployeeService;
import com.backend.portalroshkabackend.Services.HumanResource.ITHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
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
    @PutMapping("th/me/updated/{id}")
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
    public ResponseEntity<Page<UserDto>> getAllEmployees(
            @PageableDefault(size = 10, sort = "idUsuario", direction = Sort.Direction.ASC) Pageable pageable
    ){
        Page<UserDto> users = employeeService.getAllEmployees(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/th/users/active")
    public ResponseEntity<Page<UserDto>> getAllActiveEmployess(
            @PageableDefault(size = 10, sort = "id_usuario", direction = Sort.Direction.ASC)Pageable pageable // Dejar en 'id_usuario' ya que es una consulta creada manualmente en UserRepository, de forma nativa
    ){
        Page<UserDto> users = employeeService.getAllActiveEmployees(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/th/users/inactive")
    public ResponseEntity<Page<UserDto>> getAllInactiveEmployees(
            @PageableDefault(size = 10, sort = "id_usuario", direction = Sort.Direction.ASC)Pageable pageable // Lo mismo con este, dejar en 'id_usuario' independientemente de como este en el modelo Usuario
    ){
        Page<UserDto> users = employeeService.getAllInactiveEmployees(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("th/users/rol")
    public ResponseEntity<Page<UserDto>> getAllEmployeesByRol(
            @PageableDefault(size = 10, sort = "idUsuario", direction = Sort.Direction.ASC)Pageable pageable
    ){
        Page<UserDto> users = employeeService.getAllEmployeesByRol(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("th/users/equipo")
    public ResponseEntity<Page<UserDto>> getAllEmployeesByTeam(
            @PageableDefault(size = 10, sort = "idUsuario", direction = Sort.Direction.ASC)Pageable pageable
    ){
        Page<UserDto> users = employeeService.getAllEmployeesByTeam(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("th/users/cargo")
    public ResponseEntity<Page<UserDto>> getAllEmployeesByPosition(
            @PageableDefault(size = 10, sort = "idUsuario", direction = Sort.Direction.ASC)Pageable pageable
    ){
        Page<UserDto> users = employeeService.getAllEmployeesByPosition(pageable);

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

        if (user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/th/users/{id}")
    public ResponseEntity<UserDto> deleteEmployee(@PathVariable int id){
        UserDto user = employeeService.deleteEmployee(id);

        if (user == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

}

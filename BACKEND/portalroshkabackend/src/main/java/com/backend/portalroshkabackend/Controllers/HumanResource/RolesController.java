package com.backend.portalroshkabackend.Controllers.HumanResource;

import com.backend.portalroshkabackend.DTO.th.cargos.CargoByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargoInsertDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosDefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosResponseDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolDefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolInsertDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolesResponseDto;
import com.backend.portalroshkabackend.Services.HumanResource.ICommonRolesCargosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class RolesController {
    private final ICommonRolesCargosService<RolesResponseDto, RolByIdResponseDto, RolDefaultResponseDto, RolInsertDto, RolInsertDto> rolesService;

    @Autowired
    public RolesController(@Qualifier("rolesService") ICommonRolesCargosService<RolesResponseDto, RolByIdResponseDto, RolDefaultResponseDto, RolInsertDto, RolInsertDto> rolesService){
        this.rolesService = rolesService;
    }

    @GetMapping("/th/roles")
    public ResponseEntity<Page<RolesResponseDto>> getAllCargos(
            @PageableDefault(size = 10, direction = Sort.Direction.ASC) Pageable pageable

    ){
        Page<RolesResponseDto> rolesDto = rolesService.getAll(pageable);

        return ResponseEntity.ok(rolesDto);

    }

    @GetMapping("/th/roles/{idRol}")
    public ResponseEntity<?> getCargoById(
                @PathVariable int idRol
    ){
        RolByIdResponseDto response = rolesService.getById(idRol);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/th/roles")
    public ResponseEntity<RolDefaultResponseDto> addCargo(
            @RequestBody RolInsertDto insertDto
    ){
        RolDefaultResponseDto rolDto = rolesService.add(insertDto);

        return ResponseEntity.ok(rolDto);

    }

    @PutMapping("/th/roles/{idRol}")
    public ResponseEntity<RolDefaultResponseDto> updateCargo(
            @PathVariable int idRol,
            @RequestBody RolInsertDto updateDto
    ){
        RolDefaultResponseDto rolDto = rolesService.update(idRol, updateDto);

        return ResponseEntity.ok(rolDto);
    }

    @DeleteMapping("/th/roles/{idRol}")
    public ResponseEntity<RolDefaultResponseDto> deleteCargo(
            @PathVariable int idRol
    ){
        RolDefaultResponseDto rolDto = rolesService.delete(idRol);

        return ResponseEntity.ok(rolDto);
    }

}

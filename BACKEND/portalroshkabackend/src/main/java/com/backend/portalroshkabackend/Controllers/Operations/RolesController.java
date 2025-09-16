package com.backend.portalroshkabackend.Controllers.Operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.DTO.Operationes.RolesResponseDto;
import com.backend.portalroshkabackend.Services.Operations.IRolesService;

@RestController("rolesController")
@RequestMapping("/api/v1/admin/operations/rols")
public class RolesController {
    private final IRolesService rolsService;

    @Autowired
    public RolesController(IRolesService rolsService) {
        this.rolsService = rolsService;
    }

    @GetMapping("")
    public ResponseEntity<Page<RolesResponseDto>> getAllRols(
            @PageableDefault(size = 10, sort = "idRol", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<RolesResponseDto> rols = rolsService.getAllRols(pageable);
        return ResponseEntity.ok(rols);
    }
}

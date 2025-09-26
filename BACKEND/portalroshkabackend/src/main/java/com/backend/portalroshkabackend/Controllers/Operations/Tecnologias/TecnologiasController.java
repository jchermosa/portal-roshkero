package com.backend.portalroshkabackend.Controllers.Operations.Tecnologias;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.portalroshkabackend.DTO.Operationes.Tecnologias.TecnologiasRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.Tecnologias.TecnologiasResponseDto;
import com.backend.portalroshkabackend.Services.Operations.Interface.Tecnologias.ITecnologiasService;

@RestController
@RequestMapping("/api/v1/admin/operations/tecnologias")
public class TecnologiasController {

    private final ITecnologiasService tecnologiasService;

    @Autowired
    public TecnologiasController(ITecnologiasService tecnologiasService) {
        this.tecnologiasService = tecnologiasService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll(
            @PageableDefault(size = 10, sort = "idTecnologia", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<TecnologiasResponseDto> page = tecnologiasService.getAllTecnologias(pageable, "default");
        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("currentPage", page.getNumber());
        response.put("totalItems", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public TecnologiasResponseDto getById(@PathVariable Integer id) {
        return tecnologiasService.getTecnologiaById(id);
    }

    @PostMapping
    public TecnologiasResponseDto create(@RequestBody TecnologiasRequestDto dto) {
        return tecnologiasService.createTecnologia(dto);
    }

    @PutMapping("/{id}")
    public TecnologiasResponseDto update(@PathVariable Integer id, @RequestBody TecnologiasRequestDto dto) {
        return tecnologiasService.updateTecnologia(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        tecnologiasService.deleteTecnologia(id);
    }
}

package com.backend.portalroshkabackend.Controllers.HumanResource;

import com.backend.portalroshkabackend.DTO.PositionDto;
import com.backend.portalroshkabackend.DTO.PositionInsertDto;
import com.backend.portalroshkabackend.DTO.PositionUpdateDto;
import com.backend.portalroshkabackend.Services.HumanResource.CargosServiceImpl;
import com.backend.portalroshkabackend.Services.HumanResource.ICargosService;
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
public class CargosController {
    private final ICargosService cargosService;

    @Autowired
    public CargosController(ICargosService cargosService){
        this.cargosService = cargosService;
    }

    @GetMapping("th/positions")
    public ResponseEntity<Page<PositionDto>> getAllPositions(
            @PageableDefault(size = 10, sort = "idCargo", direction = Sort.Direction.ASC) Pageable pageable
    ){
        Page<PositionDto> positions = cargosService.getAllPositions(pageable);

        return ResponseEntity.ok(positions);
    }

    @GetMapping("th/positions/{id}")
    public ResponseEntity<PositionDto> getPositionById(@PathVariable int id){
        PositionDto position = cargosService.getPositionById(id);

        if (position == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(position);
    }

    @PostMapping("th/positions")
    public ResponseEntity<PositionDto> addNewPosition(@RequestBody PositionInsertDto positionInsertDto){
        PositionDto position = cargosService.addPosition(positionInsertDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/th/users/{id}")
                .buildAndExpand(position.getIdCargo())
                .toUri();

        return ResponseEntity.created(location).body(position);
    }

    @PutMapping("th/positions/{id}")
    public ResponseEntity<PositionDto> updatePosition(@RequestBody PositionUpdateDto positionUpdateDto,
                                                      @PathVariable int id){
        PositionDto position = cargosService.updatePosition(positionUpdateDto, id);

        if (position == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(position);
    }

    @DeleteMapping("th/positions/{id}")
    public ResponseEntity<PositionDto> deletePosition(@PathVariable int id){
        PositionDto position = cargosService.deletePosition(id);

        if (position == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(position);
    }
}

package com.backend.portalroshkabackend.Controllers.HumanResource;

<<<<<<< HEAD
import com.backend.portalroshkabackend.DTO.PositionDto;
import com.backend.portalroshkabackend.DTO.PositionInsertDto;
import com.backend.portalroshkabackend.DTO.PositionUpdateDto;
import com.backend.portalroshkabackend.Services.HumanResource.CargosServiceImpl;
import com.backend.portalroshkabackend.Services.HumanResource.ICargosService;
import org.springframework.beans.factory.annotation.Autowired;
=======

import com.backend.portalroshkabackend.DTO.th.cargos.*;
import com.backend.portalroshkabackend.Services.HumanResource.ICommonRolesCargosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
>>>>>>> parent of dca61a3 (se elimino backend)
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
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
=======

@RestController()
@RequestMapping("/api/v1/admin")
public class CargosController {
    private final ICommonRolesCargosService<CargosResponseDto, CargoByIdResponseDto, CargosDefaultResponseDto, CargoInsertDto, CargoUpdateDto> cargosService;

    @Autowired
    public CargosController(@Qualifier("cargosService") ICommonRolesCargosService<CargosResponseDto, CargoByIdResponseDto, CargosDefaultResponseDto, CargoInsertDto, CargoUpdateDto> cargosService){
        this.cargosService = cargosService;
    }

    @GetMapping("/th/cargos")
    public ResponseEntity<Page<CargosResponseDto>> getAllCargos(
            @PageableDefault(size = 10, direction = Sort.Direction.ASC)Pageable pageable

    ){
        Page<CargosResponseDto> cargosDto = cargosService.getAll(pageable);

        return ResponseEntity.ok(cargosDto);

    }

    @GetMapping("/th/cargos/{idCargo}")
    public ResponseEntity<?> getCargoById(
            @PathVariable int idCargo
    ){
        CargoByIdResponseDto response = cargosService.getById(idCargo);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/th/cargos")
    public ResponseEntity<CargosDefaultResponseDto> addCargo(
            @RequestBody CargoInsertDto insertDto
    ){
        CargosDefaultResponseDto cargoDto = cargosService.add(insertDto);

        return ResponseEntity.ok(cargoDto);

    }

    @PutMapping("/th/cargos/{idCargo}")
    public ResponseEntity<CargosDefaultResponseDto> updateCargo(
            @PathVariable int idCargo,
            @RequestBody CargoUpdateDto updateDto
    ){
        CargosDefaultResponseDto cargoDto = cargosService.update(idCargo, updateDto);

        return ResponseEntity.ok(cargoDto);
    }

    @DeleteMapping("/th/cargos/{idCargo}")
    public ResponseEntity<CargosDefaultResponseDto> deleteCargo(
            @PathVariable int idCargo
    ){
        CargosDefaultResponseDto cargoDto = cargosService.delete(idCargo);

        return ResponseEntity.ok(cargoDto);
    }


>>>>>>> parent of dca61a3 (se elimino backend)
}

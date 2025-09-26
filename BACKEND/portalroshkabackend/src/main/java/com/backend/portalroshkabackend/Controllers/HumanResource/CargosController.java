package com.backend.portalroshkabackend.Controllers.HumanResource;


import com.backend.portalroshkabackend.DTO.th.cargos.CargoByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargoInsertDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosDefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosResponseDto;
import com.backend.portalroshkabackend.Services.HumanResource.ICommonRolesCargosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/admin")
public class CargosController {
    private final ICommonRolesCargosService<CargosResponseDto, CargoByIdResponseDto, CargosDefaultResponseDto, CargoInsertDto, CargoInsertDto> cargosService;

    @Autowired
    public CargosController(@Qualifier("cargosService") ICommonRolesCargosService<CargosResponseDto, CargoByIdResponseDto, CargosDefaultResponseDto, CargoInsertDto, CargoInsertDto> cargosService){
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
            @RequestBody CargoInsertDto updateDto
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


}

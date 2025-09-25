package com.backend.portalroshkabackend.Controllers.Operations;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.portalroshkabackend.DTO.Operationes.DiasLaboralDto;
import com.backend.portalroshkabackend.Services.Operations.Interface.IDiasLaboralesService;

@RestController("DiasLaboralesController")
@RequestMapping("/api/v1/admin/operations")
public class DiasLaboralesController {

    private final IDiasLaboralesService diasLaboralesService;

    public DiasLaboralesController(IDiasLaboralesService diasLaboralesService) {
        this.diasLaboralesService = diasLaboralesService;
    }

    @GetMapping("/diaslaborales")
    public List<DiasLaboralDto> getAllDias(){
        return diasLaboralesService.getAllDias();
    }
}

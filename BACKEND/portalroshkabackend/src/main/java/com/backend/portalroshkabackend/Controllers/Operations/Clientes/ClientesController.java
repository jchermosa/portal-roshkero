package com.backend.portalroshkabackend.Controllers.Operations.Clientes;

import com.backend.portalroshkabackend.DTO.Operationes.Clientes.ClientesRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.Clientes.ClientesResponseDto;
import com.backend.portalroshkabackend.Services.Operations.Interface.IClientesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController("clientesController")
@RequestMapping("/api/v1/admin/operations")
public class ClientesController {

    private final IClientesService clientesService;

    @Autowired
    public ClientesController(IClientesService clientesService) {
        this.clientesService = clientesService;
    }

    @GetMapping("/clientes")
    public ResponseEntity<Map<String, Object>> getAllClientes(
            @PageableDefault(size = 10, sort = "idCliente", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "default") String sortBy) {

        Page<ClientesResponseDto> page = clientesService.getAllClientes(pageable, sortBy);

        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("currentPage", page.getNumber());
        response.put("totalItems", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<ClientesResponseDto> getClienteById(@PathVariable Integer id) {
        return ResponseEntity.ok(clientesService.getClienteById(id));
    }

    @PostMapping("/clientes")
    public ResponseEntity<ClientesResponseDto> createCliente(@RequestBody ClientesRequestDto dto) {
        return ResponseEntity.ok(clientesService.createCliente(dto));
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<ClientesResponseDto> updateCliente(@PathVariable Integer id,
            @RequestBody ClientesRequestDto dto) {
        return ResponseEntity.ok(clientesService.updateCliente(id, dto));
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Integer id) {
        clientesService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }
}

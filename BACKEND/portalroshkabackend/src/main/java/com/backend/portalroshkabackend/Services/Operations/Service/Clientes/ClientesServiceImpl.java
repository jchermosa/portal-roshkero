package com.backend.portalroshkabackend.Services.Operations.Service.Clientes;

import com.backend.portalroshkabackend.DTO.Operationes.Clientes.ClientesRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.Clientes.ClientesResponseDto;
import com.backend.portalroshkabackend.Models.Clientes;
import com.backend.portalroshkabackend.Repositories.ClientesRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.IClientesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClientesServiceImpl implements IClientesService {

    private final ClientesRepository clienteRepository;

    @Autowired
    public ClientesServiceImpl(ClientesRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    private ClientesResponseDto toDto(Clientes clientes) {
        return new ClientesResponseDto(
                clientes.getIdCliente(),
                clientes.getNombre(),
                clientes.getNroTelefono(),
                clientes.getCorreo(),
                clientes.getRuc(),
                clientes.getFechaCreacion());
    }

    private Clientes fromDto(ClientesRequestDto dto) {
        Clientes clientes = new Clientes();
        clientes.setNombre(dto.getNombre());
        clientes.setNroTelefono(dto.getNroTelefono());
        clientes.setCorreo(dto.getCorreo());
        clientes.setRuc(dto.getRuc());
        clientes.setFechaCreacion(LocalDateTime.now());
        return clientes;
    }

    @Override
    public Page<ClientesResponseDto> getAllClientes(Pageable pageable, String sortBy) {
        return clienteRepository.findAll(pageable).map(this::toDto);
    }

    @Override
    public ClientesResponseDto getClienteById(Integer id) {
        Clientes clientes = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente not found: " + id));
        return toDto(clientes);
    }

    @Override
    public ClientesResponseDto createCliente(ClientesRequestDto dto) {
        Clientes clientes = fromDto(dto);
        Clientes saved = clienteRepository.save(clientes);
        return toDto(saved);
    }

    @Override
    public ClientesResponseDto updateCliente(Integer id, ClientesRequestDto dto) {
        Clientes clientes = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente not found: " + id));

        clientes.setNombre(dto.getNombre());
        clientes.setNroTelefono(dto.getNroTelefono());
        clientes.setCorreo(dto.getCorreo());
        clientes.setRuc(dto.getRuc());

        Clientes updated = clienteRepository.save(clientes);
        return toDto(updated);
    }

    @Override
    public void deleteCliente(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente not found: " + id);
        }
        clienteRepository.deleteById(id);
    }
}

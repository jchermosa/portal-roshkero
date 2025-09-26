package com.backend.portalroshkabackend.Services.Operations.Interface.Clientes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.backend.portalroshkabackend.DTO.Operationes.Clientes.ClientesRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.Clientes.ClientesResponseDto;

public interface IClientesService {
    Page<ClientesResponseDto> getAllClientes(Pageable pageable, String sortBy);

    ClientesResponseDto getClienteById(Integer id);

    ClientesResponseDto createCliente(ClientesRequestDto dto);

    ClientesResponseDto updateCliente(Integer id, ClientesRequestDto dto);

    void deleteCliente(Integer id);
}
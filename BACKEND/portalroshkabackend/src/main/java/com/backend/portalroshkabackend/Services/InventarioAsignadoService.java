package com.backend.portalroshkabackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.portalroshkabackend.DTO.InventarioAsignadoDto;
import com.backend.portalroshkabackend.Models.InventarioAsignado;
import com.backend.portalroshkabackend.Repositories.InventarioAsignadoRepository;
import com.backend.portalroshkabackend.Repositories.DispositivoRepository;

@Service
public class InventarioAsignadoService {

    @Autowired
    private InventarioAsignadoRepository inventarioAsignadoRepository;
    
    @Autowired
    private DispositivoRepository dispositivoRepository;

    public InventarioAsignadoService(InventarioAsignadoRepository inventarioAsignadoRepository,
                                   DispositivoRepository dispositivoRepository) {
        this.inventarioAsignadoRepository = inventarioAsignadoRepository;
        this.dispositivoRepository = dispositivoRepository;
    }

    @Transactional
    public InventarioAsignadoDto crearInventarioAsignado(InventarioAsignadoDto inventarioAsignadoDto) {
        // Validar el estado
        com.backend.portalroshkabackend.Models.Enum.EstadoAsignacion estadoEnum = inventarioAsignadoDto.getEstado();
        if (estadoEnum == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        
        // Crear el objeto con todos los datos
        InventarioAsignado inventarioAsignado = new InventarioAsignado();
        inventarioAsignado.setFechaAsignacion(inventarioAsignadoDto.getFechaAsignacion());
        inventarioAsignado.setFechaDevolucion(inventarioAsignadoDto.getFechaDevolucion());
        
        // Buscar y asignar el dispositivo por ID
        if (inventarioAsignadoDto.getIdInventario() != null) {
            dispositivoRepository.findById(inventarioAsignadoDto.getIdInventario())
                .ifPresent(inventarioAsignado::setIdInventario);
        }
        
        inventarioAsignado.setEstado(estadoEnum);
        
        // Por ahora, no asignar idSolicitudDispositivos hasta que se cree el repositorio correspondiente
        // TODO: Crear SolicitudDispositivosRepository e implementar el mapeo
       
        InventarioAsignado savedInventario = inventarioAsignadoRepository.save(inventarioAsignado);
        
        return mapToDto(savedInventario);
    }
    
    private InventarioAsignadoDto mapToDto(InventarioAsignado inventarioAsignado) {
        InventarioAsignadoDto dto = new InventarioAsignadoDto();
        dto.setFechaAsignacion(inventarioAsignado.getFechaAsignacion());
        dto.setFechaDevolucion(inventarioAsignado.getFechaDevolucion());
        dto.setIdInventario(inventarioAsignado.getIdInventario() != null ? 
            inventarioAsignado.getIdInventario().getIdInventario() : null);
        dto.setEstado(inventarioAsignado.getEstado());
        dto.setIdSolicitudDispositivos(inventarioAsignado.getIdSolicitudDispositivos() != null ? 
            inventarioAsignado.getIdSolicitudDispositivos().getIdSolicitudDispositivo() : null);
        return dto;
    }
}

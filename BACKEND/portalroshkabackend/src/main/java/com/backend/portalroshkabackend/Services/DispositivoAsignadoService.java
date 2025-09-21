package com.backend.portalroshkabackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.portalroshkabackend.DTO.DispositivoAsignadoDto;
import com.backend.portalroshkabackend.Models.DispositivoAsignado;
import com.backend.portalroshkabackend.Repositories.InventarioAsignadoRepository;
import com.backend.portalroshkabackend.Repositories.DispositivoRepository;

@Service
public class DispositivoAsignadoService {

    @Autowired
    private InventarioAsignadoRepository inventarioAsignadoRepository;
    
    @Autowired
    private DispositivoRepository dispositivoRepository;

    public DispositivoAsignadoService(InventarioAsignadoRepository inventarioAsignadoRepository,
                                   DispositivoRepository dispositivoRepository) {
        this.inventarioAsignadoRepository = inventarioAsignadoRepository;
        this.dispositivoRepository = dispositivoRepository;
    }

    @Transactional
    public DispositivoAsignadoDto crearInventarioAsignado(DispositivoAsignadoDto inventarioAsignadoDto) {
        // Validar el estado
        com.backend.portalroshkabackend.Models.Enum.EstadoAsignacion estadoEnum = inventarioAsignadoDto.getEstado();
        if (estadoEnum == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        
        // Crear el objeto con todos los datos
        DispositivoAsignado inventarioAsignado = new DispositivoAsignado();
        inventarioAsignado.setFechaAsignacion(inventarioAsignadoDto.getFechaAsignacion());
        inventarioAsignado.setFechaDevolucion(inventarioAsignadoDto.getFechaDevolucion());
        
        // Buscar y asignar el tipo de dispositivo por ID
        if (inventarioAsignadoDto.getIdInventario() != null) {
            dispositivoRepository.findById(inventarioAsignadoDto.getIdInventario())
                .ifPresent(inventarioAsignado::setIdTipoDispositivo);
        }
        
        inventarioAsignado.setEstado(estadoEnum);
        
        // Por ahora, no asignar idSolicitudDispositivos hasta que se cree el repositorio correspondiente
        // TODO: Crear SolicitudDispositivosRepository e implementar el mapeo
       
        DispositivoAsignado savedInventario = inventarioAsignadoRepository.save(inventarioAsignado);
        
        return mapToDto(savedInventario);
    }
    
    private DispositivoAsignadoDto mapToDto(DispositivoAsignado inventarioAsignado) {
        DispositivoAsignadoDto dto = new DispositivoAsignadoDto();
        dto.setFechaAsignacion(inventarioAsignado.getFechaAsignacion());
        dto.setFechaDevolucion(inventarioAsignado.getFechaDevolucion());
        dto.setIdInventario(inventarioAsignado.getIdTipoDispositivo() != null ? 
            inventarioAsignado.getIdTipoDispositivo().getIdTipoDispositivo() : null);
        dto.setEstado(inventarioAsignado.getEstado());
        dto.setIdSolicitudDispositivos(inventarioAsignado.getIdSolicitudDispositivos() != null ? 
            inventarioAsignado.getIdSolicitudDispositivos().getIdSolicitudDispositivo() : null);
        return dto;
    }
}

package com.backend.portalroshkabackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.portalroshkabackend.DTO.DispositivoAsignadoDto;
import com.backend.portalroshkabackend.Models.DispositivoAsignado;
import com.backend.portalroshkabackend.Repositories.InventarioAsignadoRepository;
import java.time.LocalDate;

@Service
public class DispositivoAsignadoService {

    @Autowired
    private InventarioAsignadoRepository inventarioAsignadoRepository;

    public DispositivoAsignadoService(InventarioAsignadoRepository inventarioAsignadoRepository) {
        this.inventarioAsignadoRepository = inventarioAsignadoRepository;
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
        inventarioAsignado.setFechaEntrega(inventarioAsignadoDto.getFechaAsignacion());
        inventarioAsignado.setFechaDevolucion(inventarioAsignadoDto.getFechaDevolucion());
        //inventarioAsignado.setFechaCreacion(LocalDate.now());
        
        // No asignamos directamente un tipo de dispositivo sino que debemos
        // primero tener un dispositivo en la base de datos
        // Por ahora dejamos esto sin asignar, se debe implementar en otro método
        // que obtenga un dispositivo disponible y lo asigne
        
        inventarioAsignado.setEstado(estadoEnum);
        
        // Por ahora, no asignar idSolicitudDispositivos hasta que se cree el repositorio correspondiente
        // TODO: Crear SolicitudDispositivosRepository e implementar el mapeo
       
        DispositivoAsignado savedInventario = inventarioAsignadoRepository.save(inventarioAsignado);
        
        return mapToDto(savedInventario);
    }
    
    private DispositivoAsignadoDto mapToDto(DispositivoAsignado inventarioAsignado) {
        DispositivoAsignadoDto dto = new DispositivoAsignadoDto();
        //dto.setFechaAsignacion(inventarioAsignado.getFechaCreacion());
        dto.setFechaDevolucion(inventarioAsignado.getFechaDevolucion());
        dto.setIdInventario(inventarioAsignado.getDispositivo() != null && 
            inventarioAsignado.getDispositivo().getIdDispositivo() != null ? 
            inventarioAsignado.getDispositivo().getIdDispositivo() : null);
        dto.setEstado(inventarioAsignado.getEstado());
        dto.setIdSolicitudDispositivos(inventarioAsignado.getSolicitud() != null ? 
            inventarioAsignado.getSolicitud().getIdSolicitud() : null);
        return dto;
    }
}

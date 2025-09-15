package com.backend.portalroshkabackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.DTO.InventarioAsignadoDto;
import com.backend.portalroshkabackend.Models.EstadoAsignacion;
import com.backend.portalroshkabackend.Models.InventarioAsignado;
import com.backend.portalroshkabackend.Repositories.InventarioAsignadoRepository;

@Service
public class InventarioAsignadoService {


    @Autowired
    private InventarioAsignadoRepository inventarioAsignadoRepository;

    InventarioAsignadoService(InventarioAsignadoRepository inventarioAsignadoRepository) {
        this.inventarioAsignadoRepository = inventarioAsignadoRepository;
    }

    
    @Transactional
    public InventarioAsignado crearInventarioAsignado(InventarioAsignadoDto inventarioAsignadoDto) {
        // Si el problema persiste después de todas las soluciones, podemos usar SQL nativo con casting explícito
        
        // Validar el estado
        EstadoAsignacion estadoEnum = inventarioAsignadoDto.getEstado();
        if (estadoEnum == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        
        // Crear el objeto con todos los datos
        InventarioAsignado inventarioAsignado = new InventarioAsignado();
        inventarioAsignado.setFechaAsignacion(inventarioAsignadoDto.getFechaAsignacion());
        inventarioAsignado.setFechaDevolucion(inventarioAsignadoDto.getFechaDevolucion());
        inventarioAsignado.setIdInventario(inventarioAsignadoDto.getIdInventario());
        inventarioAsignado.setEstado(estadoEnum);
        inventarioAsignado.setIdSolicitudDispositivos(inventarioAsignadoDto.getIdSolicitudDispositivos());
       
        return inventarioAsignadoRepository.save(inventarioAsignado);
    }
}

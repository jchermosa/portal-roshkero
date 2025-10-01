package com.backend.portalroshkabackend.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.DTO.InventarioAsignadoDto;
import com.backend.portalroshkabackend.Exceptions.ResourceNotFoundException;
import com.backend.portalroshkabackend.Models.DispositivoAsignado;
import com.backend.portalroshkabackend.Models.SolicitudDispositivos;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Repositories.InventarioAsignadoRepository;
import com.backend.portalroshkabackend.Repositories.TipoDispositivoRepository;

@Service
public class InventarioAsignadoService {

    private final InventarioAsignadoRepository inventarioAsignadoRepository;
    private final TipoDispositivoRepository tipoDispositivoRepository;

    @Autowired
    public InventarioAsignadoService(InventarioAsignadoRepository inventarioAsignadoRepository,
                                   TipoDispositivoRepository tipoDispositivoRepository) {
        this.inventarioAsignadoRepository = inventarioAsignadoRepository;
        this.tipoDispositivoRepository = tipoDispositivoRepository;
    }

    @Transactional
    public InventarioAsignadoDto crearInventarioAsignado(InventarioAsignadoDto inventarioAsignadoDto) {
        // Validar el estado
        com.backend.portalroshkabackend.Models.Enum.EstadoAsignacion estadoEnum = inventarioAsignadoDto.getEstado();
        if (estadoEnum == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        
        // Crear el objeto con todos los datos
        DispositivoAsignado inventarioAsignado = new DispositivoAsignado();
        inventarioAsignado.setFechaAsignacion(inventarioAsignadoDto.getFechaAsignacion() != null ? 
                inventarioAsignadoDto.getFechaAsignacion() : LocalDate.now());
        inventarioAsignado.setFechaDevolucion(inventarioAsignadoDto.getFechaDevolucion());
        inventarioAsignado.setFechaCreacion(LocalDateTime.now());
        
        // Buscar y asignar el tipo de dispositivo por ID
        if (inventarioAsignadoDto.getIdInventario() != null) {
            TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(inventarioAsignadoDto.getIdInventario())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de dispositivo no encontrado con id: " + inventarioAsignadoDto.getIdInventario()));
            inventarioAsignado.setIdTipoDispositivo(tipoDispositivo);
        }
        
        inventarioAsignado.setEstado(estadoEnum);
        
        // Nota: la solicitud de dispositivos se manejará cuando exista el repositorio correspondiente
       
        DispositivoAsignado savedInventario = inventarioAsignadoRepository.save(inventarioAsignado);
        
        return mapToDto(savedInventario);
    }
    
    @Transactional(readOnly = true)
    public List<InventarioAsignadoDto> getAllAsignaciones() {
        List<DispositivoAsignado> dispositivosAsignados = inventarioAsignadoRepository.findAll();
        return dispositivosAsignados.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InventarioAsignadoDto getAsignacionById(Integer id) {
        DispositivoAsignado dispositivoAsignado = inventarioAsignadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada con id: " + id));
        return mapToDto(dispositivoAsignado);
    }

    @Transactional
    public InventarioAsignadoDto updateAsignacion(Integer id, InventarioAsignadoDto inventarioAsignadoDto) {
        DispositivoAsignado existingDispositivo = inventarioAsignadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada con id: " + id));

        // Actualizar campos
        if (inventarioAsignadoDto.getFechaAsignacion() != null) {
            existingDispositivo.setFechaAsignacion(inventarioAsignadoDto.getFechaAsignacion());
        }
        
        existingDispositivo.setFechaDevolucion(inventarioAsignadoDto.getFechaDevolucion());
        
        if (inventarioAsignadoDto.getIdInventario() != null) {
            TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(inventarioAsignadoDto.getIdInventario())
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de dispositivo no encontrado con id: " 
                            + inventarioAsignadoDto.getIdInventario()));
            existingDispositivo.setIdTipoDispositivo(tipoDispositivo);
        }
        
        if (inventarioAsignadoDto.getEstado() != null) {
            existingDispositivo.setEstado(inventarioAsignadoDto.getEstado());
        }

        DispositivoAsignado updatedDispositivo = inventarioAsignadoRepository.save(existingDispositivo);
        
        return mapToDto(updatedDispositivo);
    }

    @Transactional
    public void deleteAsignacion(Integer id) {
        if (!inventarioAsignadoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Asignación no encontrada con id: " + id);
        }
        inventarioAsignadoRepository.deleteById(id);
    }
    
    private InventarioAsignadoDto mapToDto(DispositivoAsignado inventarioAsignado) {
        InventarioAsignadoDto dto = new InventarioAsignadoDto();
        dto.setIdDispositivoAsignado(inventarioAsignado.getIdDispositivoAsignado());
        dto.setFechaAsignacion(inventarioAsignado.getFechaAsignacion());
        dto.setFechaDevolucion(inventarioAsignado.getFechaDevolucion());
        dto.setFechaCreacion(inventarioAsignado.getFechaCreacion());
        
        if (inventarioAsignado.getIdTipoDispositivo() != null) {
            dto.setIdInventario(inventarioAsignado.getIdTipoDispositivo().getIdInventario());
            dto.setNombreDispositivo(inventarioAsignado.getIdTipoDispositivo().getNombre());
        }
        
        dto.setEstado(inventarioAsignado.getEstado());
        
        if (inventarioAsignado.getIdSolicitudDispositivos() != null) {
            dto.setIdSolicitudDispositivos(inventarioAsignado.getIdSolicitudDispositivos().getIdSolicitudDispositivo());
        }
        
        return dto;
    }
}

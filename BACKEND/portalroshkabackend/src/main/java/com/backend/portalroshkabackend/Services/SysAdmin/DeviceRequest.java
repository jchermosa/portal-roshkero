package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceRequestDto;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.Models.Dispositivo;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceRepository;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceRequestRepository;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceTypesRepository;

import jakarta.transaction.Transactional;

@Service
public class DeviceRequest {

    @Autowired
    private DeviceRequestRepository deviceRequestRepository;

    @Autowired 
    private DeviceTypesRepository deviceTypesRepository;

    @Autowired 
    private DeviceRepository deviceRepository;


    DeviceRequest(DeviceRequestRepository deviceRequestRepository, DeviceRepository deviceRepository) {
        this.deviceRequestRepository = deviceRequestRepository;
        this.deviceRepository = deviceRepository;
    }


    @Transactional
    public DeviceRequestDto acceptRequest(Integer idRequest){

        Optional<Solicitud> solicitudOp = deviceRequestRepository.findById(idRequest);
        if (solicitudOp.isEmpty()) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + idRequest);
        }

        Solicitud solicitud = solicitudOp.get();

        // Extraer el ID del tipo de dispositivo del comentario
        Integer idTipoDispositivo = extractDeviceTypeIdFromComment(solicitud.getComentario());
        if (idTipoDispositivo == null) {
            throw new RuntimeException("No se pudo extraer el tipo de dispositivo del comentario: " + solicitud.getComentario());
        }

        // Verificar que hay dispositivos disponibles del tipo solicitado
        if (!isDeviceTypeAvailable(idTipoDispositivo)) {
            String nombreDispositivo = deviceTypesRepository.findById(idTipoDispositivo)
                .map(TipoDispositivo::getNombre)
                .orElse("Desconocido");
            throw new RuntimeException("No hay dispositivos disponibles del tipo solicitado (ID: " + idTipoDispositivo + ", Nombre: " + nombreDispositivo + ").");
        }

        if(solicitud.getEstado() != EstadoSolicitudEnum.P) {
            throw new RuntimeException("La solicitud ya fue procesada.");
        }

        solicitud.setEstado(EstadoSolicitudEnum.A);
        deviceRequestRepository.save(solicitud);

        return convertToDto(solicitud);
    }

    @Transactional
    public DeviceRequestDto rejectRequest(Integer idRequest) {
        Optional<Solicitud> solicitudOp = deviceRequestRepository.findById(idRequest);

        if (solicitudOp.isEmpty()) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + idRequest);
        }

        Solicitud solicitud = solicitudOp.get();


        if(solicitud.getEstado() != EstadoSolicitudEnum.P) {
            throw new RuntimeException("La solicitud ya fue procesada.");
        }

        solicitud.setEstado(EstadoSolicitudEnum.R);
        deviceRequestRepository.save(solicitud);
        return convertToDto(solicitud);
    }


    private DeviceRequestDto convertToDto(Solicitud solicitud) {
        DeviceRequestDto dto = new DeviceRequestDto();
        dto.setFechaInicio(solicitud.getFechaInicio());
        // dto.setCantDias(solicitud.getCantDias());
        dto.setEstado(solicitud.getEstado());
        dto.setComentario(solicitud.getComentario());
        dto.setIdUsuario(solicitud.getUsuario().getIdUsuario());
        dto.setIdTipoDispositivo(solicitud.getIdSolicitud());
        return dto;
    }

    private DeviceTypeDTO convertToDto(TipoDispositivo tipoDispositivo) {
        DeviceTypeDTO dto = new DeviceTypeDTO();
        dto.setIdTipoDispositivo(tipoDispositivo.getIdTipoDispositivo());
        dto.setNombre(tipoDispositivo.getNombre());
        dto.setDetalle(tipoDispositivo.getDetalle());
        return dto;
    }


    private Integer extractDeviceTypeIdFromComment(String comentario) {
        if (comentario == null || comentario.trim().isEmpty()) {
            return null;
        }

        // Patrón regex para encontrar números entre paréntesis al inicio del string
        Pattern pattern = Pattern.compile("^\\((\\d+)\\)");
        Matcher matcher = pattern.matcher(comentario.trim());

        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private boolean isDeviceTypeAvailable(Integer idTipoDispositivo) {
        if (idTipoDispositivo == null) {
            return false;
        }

        // Verificar que el tipo de dispositivo existe
        Optional<TipoDispositivo> tipoDispositivoOp = deviceTypesRepository.findById(idTipoDispositivo);
        if (tipoDispositivoOp.isEmpty()) {
            return false;
        }

        TipoDispositivo tipoDispositivo = tipoDispositivoOp.get();

        // Obtener dispositivos sin dueño
        List<Dispositivo> dispositivosSinDuenio = deviceRepository.findAllWithoutOwner();
        
        // Verificar si hay dispositivos disponibles del tipo solicitado
        return dispositivosSinDuenio.stream()
            .anyMatch(d -> d.getTipoDispositivo().getIdTipoDispositivo().equals(tipoDispositivo.getIdTipoDispositivo()));
    }

}
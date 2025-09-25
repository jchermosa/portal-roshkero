package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.DeviceRequestDto;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.Models.Dispositivo;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Repositories.DeviceRequestRepository;
import com.backend.portalroshkabackend.Repositories.SYSADMIN.DeviceTypesRepository;

import jakarta.transaction.Transactional;

@Service
public class DeviceRequest {

    @Autowired
    private DeviceRequestRepository deviceRequestRepository;


    DeviceRequest(DeviceRequestRepository deviceRequestRepository) {
        this.deviceRequestRepository = deviceRequestRepository;
    }


    @Transactional
    public DeviceRequestDto acceptRequest(Integer idRequest){
        Optional<Solicitud> solicitudOp = deviceRequestRepository.findById(idRequest);
        if (solicitudOp.isEmpty()) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + idRequest);
        }

        Solicitud solicitud = solicitudOp.get();

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
        dto.setCantDias(solicitud.getCantDias());
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
}
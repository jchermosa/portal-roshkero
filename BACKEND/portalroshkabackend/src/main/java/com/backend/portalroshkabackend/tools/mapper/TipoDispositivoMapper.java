package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import java.time.LocalDateTime;

public class TipoDispositivoMapper {

    // ===== ENTIDAD A DTO =====
    public static DeviceTypeDTO toDeviceTypeDto(TipoDispositivo tipoDispositivo) {
        if (tipoDispositivo == null) return null;

        DeviceTypeDTO dto = new DeviceTypeDTO();
        dto.setIdTipoDispositivo(tipoDispositivo.getIdTipoDispositivo());
        dto.setNombre(tipoDispositivo.getNombre());
        dto.setDetalle(tipoDispositivo.getDetalle());
        return dto;
    }

    // ===== DTO A ENTIDAD =====
    public static void toTipoDispositivoFromDto(TipoDispositivo tipoDispositivo, DeviceTypeDTO dto) {
        if (dto == null) return;

        tipoDispositivo.setNombre(dto.getNombre());
        tipoDispositivo.setDetalle(dto.getDetalle());
    }


}
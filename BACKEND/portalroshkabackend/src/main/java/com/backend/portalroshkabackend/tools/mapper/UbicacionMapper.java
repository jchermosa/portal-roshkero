package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.SYSADMIN.UbicacionDto;
import com.backend.portalroshkabackend.Models.Ubicacion;

import java.util.Collections;

public class UbicacionMapper {
    public static UbicacionDto toDTO(Ubicacion ubicacion) {
        if (ubicacion == null) {
            return null;
        }
        UbicacionDto dto = new UbicacionDto();
        dto.setIdUbicacion(ubicacion.getIdUbicacion());
        dto.setNombre(ubicacion.getNombre());
        dto.setEstado(ubicacion.getEstado());
        dto.setDispositivos(Collections.emptyList());
        return dto;
    }

    public static void toUbicacionFromUbicacionDto(Ubicacion ubicacion, UbicacionDto dto) {
        if (dto == null) return;
        ubicacion.setIdUbicacion(dto.getIdUbicacion());
        ubicacion.setNombre(dto.getNombre());
        ubicacion.setEstado(dto.getEstado());
    }
}

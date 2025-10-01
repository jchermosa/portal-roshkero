package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.UbicacionDto;
import com.backend.portalroshkabackend.Models.Dispositivo;
import com.backend.portalroshkabackend.Models.Ubicacion;

public class DispositivoMapper {

    // DTO a Entidad
    public static void toUbicacionFromUbicacionDto(Ubicacion ubicacion, UbicacionDto dto) {
        if (dto == null) return;

        ubicacion.setIdUbicacion(dto.getIdUbicacion());
        ubicacion.setNombre(dto.getNombre());
        ubicacion.setEstado(dto.getEstado());
    }

    //DTO a Entidad
    public static void toDispositivoFromDto(Dispositivo dispositivo, DeviceDTO dto) {
        if (dto == null) return;

        dispositivo.setNroSerie(dto.getNroSerie());
        dispositivo.setModelo(dto.getModelo());
        dispositivo.setFechaFabricacion(dto.getFechaFabricacion());
        dispositivo.setCategoria(dto.getCategoria());
        dispositivo.setDetalles(dto.getDetalle());
        dispositivo.setEstado(dto.getEstado());
    }

    // Entidad a DTO
    public static UbicacionDto toUbicacionDto(Ubicacion ubicacion) {
        if (ubicacion == null) return null;

        UbicacionDto dto = new UbicacionDto();
        dto.setIdUbicacion(ubicacion.getIdUbicacion());
        dto.setNombre(ubicacion.getNombre());
        dto.setEstado(ubicacion.getEstado());
        return dto;
    }

    //Entidad a DTO
    public static DeviceDTO toDeviceDto(Dispositivo dispositivo) {
        if (dispositivo == null) return null;

        DeviceDTO dto = new DeviceDTO();
        dto.setIdDispositivo(dispositivo.getIdDispositivo());
        dto.setNroSerie(dispositivo.getNroSerie());
        dto.setModelo(dispositivo.getModelo());
        dto.setFechaFabricacion(dispositivo.getFechaFabricacion());
        dto.setCategoria(dispositivo.getCategoria());
        dto.setDetalle(dispositivo.getDetalles());
        dto.setEstado(dispositivo.getEstado());

        // Mapeo seguro de relaciones
        if (dispositivo.getEncargado() != null) {
            dto.setEncargado(dispositivo.getEncargado().getIdUsuario());
        }

        if (dispositivo.getTipoDispositivo() != null) {
            dto.setTipoDispositivo(dispositivo.getTipoDispositivo().getIdTipoDispositivo());
        }

        if (dispositivo.getUbicacion() != null) {
            dto.setUbicacion(dispositivo.getUbicacion().getIdUbicacion());
        }

        return dto;
    }
}

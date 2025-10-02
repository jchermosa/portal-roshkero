package com.backend.portalroshkabackend.DTO.SYSADMIN;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.backend.portalroshkabackend.Models.Enum.CategoriaEnum;
import com.backend.portalroshkabackend.Models.Enum.EstadoInventario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {

    private Integer idDispositivo;
    private String nombreDispositivo;
    private Integer tipoDispositivo;
    private String nroSerie;
    private String modelo;
    private String detalle;
    private LocalDate fechaFabricacion;
    private EstadoInventario estado;
    private CategoriaEnum categoria;
    private Integer encargado;
    private String nombreEncargado;
    private Integer ubicacion;
    


}

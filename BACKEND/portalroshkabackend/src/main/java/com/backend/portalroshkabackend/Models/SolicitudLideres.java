package com.backend.portalroshkabackend.Models;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Data
@Table(name = "solicitud_lideres")
public class SolicitudLideres {


    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @Column(name = "id_lider")
    private Integer idLider;


    @Column(name = "fecha_asignacion")
    private Date finVigencia;

    
}

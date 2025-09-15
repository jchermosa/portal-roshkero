package com.backend.portalroshkabackend.Models;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Data
@Table(name = "solicitud_lideres")
public class SolicitudLideres {


    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer idSolicitudLideres;

    @JoinColumn(name = "id_solicitud")
    @ManyToOne
    private SolicitudesTH idSolicitud;

    
    @JoinColumn(name = "id_usuario")
    @ManyToOne
    private  Usuario idUsuario;


    @Column(name = "fecha_asignacion")
    private Date finVigencia;

    
}

package com.backend.portalroshkabackend.Models;

import java.time.LocalDateTime;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@NoArgsConstructor
@Data
@Table(name = "solicitud_lideres")
public class SolicitudLideres {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer idSolicitudLider;

    @JoinColumn(name = "id_lider")
    @OneToOne
    private Usuario usuario;

    @JoinColumn(name = "id_solicitud_th")
    @ManyToOne
    private SolicitudesTH solicitudesTH;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "estado_solicitud_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoSolicitudEnum estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    
}

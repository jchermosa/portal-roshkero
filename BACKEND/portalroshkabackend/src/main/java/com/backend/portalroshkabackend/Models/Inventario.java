package com.backend.portalroshkabackend.Models;

import com.backend.portalroshkabackend.Models.Enum.*;
import java.sql.Date;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "dispositivos")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dispositivo")
    private Integer idDispositivo;

    @ManyToOne
    @JoinColumn(name = "id_tipo_dispositivo")
    private TipoDispositivo idTipoInventario;

    @Column(name = "nro_serie")
    private String nroSerie;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "detalles")
    private String detalles;

    @Column(name = "fecha_fabricacion")
    private Date fechaFabricacion;

    // Para enums PostgreSQL se debe especificar así y asegurar que los valores coinciden exactamente
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "estado_inventario_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoInventario estado;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    
}

package com.backend.portalroshkabackend.Models;

import com.backend.portalroshkabackend.Models.Enum.CategoriaEnum;
import com.backend.portalroshkabackend.Models.Enum.EstadoInventario;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "dispositivos")
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dispositivo")
    private Integer idDispositivo;

    @ManyToOne
    @JoinColumn(name = "id_tipo_dispositivo")
    private TipoDispositivo tipoDispositivo;

    @Column(name = "nro_serie", nullable = false)
    private String nroSerie;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "detalles", nullable = false)
    private String detalles;

    @Column(name = "fecha_fabricacion", nullable = false)
    private LocalDate fechaFabricacion;
    
    @OneToMany(mappedBy = "dispositivo")
    private List<DispositivoAsignado> dispositivosAsignados = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_ubicacion")
    private Ubicacion ubicacion;

    // Para enums PostgreSQL se debe especificar así y asegurar que los valores coinciden exactamente
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "estado_inventario_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoInventario estado = EstadoInventario.D;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, columnDefinition = "categoria_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private CategoriaEnum categoria = CategoriaEnum.OFICINA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encargado")
    private Usuario encargado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
}

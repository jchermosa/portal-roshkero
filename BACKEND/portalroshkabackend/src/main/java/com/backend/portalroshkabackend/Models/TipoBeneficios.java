package com.backend.portalroshkabackend.Models;

import java.math.BigDecimal;
// import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
// import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
// import java.util.ArrayList;

@Entity
@NoArgsConstructor
@Data
@Table(name = "tipo_beneficios")
public class TipoBeneficios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_beneficio")
    private Integer idTipoBeneficio;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "vigencia", nullable = false, columnDefinition = "estado_ac_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoActivoInactivo vigencia = EstadoActivoInactivo.A;

    @Column(name = "monto_maximo", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoMaximo;

//    @OneToMany(mappedBy = "beneficio", fetch = FetchType.LAZY)
//    private List<BeneficiosAsignados> beneficiosAsignados = new ArrayList<>();

}

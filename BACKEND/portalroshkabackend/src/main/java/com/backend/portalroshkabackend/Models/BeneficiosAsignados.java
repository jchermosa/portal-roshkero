package com.backend.portalroshkabackend.Models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@Entity
@Table(name = "beneficio_asignado")
public class BeneficiosAsignados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_beneficio_asignado")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_solicitud")
    private Solicitud solicitud;

    @ManyToOne
    @JoinColumn(name = "id_tipo_beneficio")
    private TipoBeneficios beneficio;

    @Column(name = "monto_aprobado", precision = 10, scale = 2)
    private BigDecimal montoAprobado ;

    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion;

}

package com.backend.portalroshkabackend.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "dias_laborales")
public class DiaLaboral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dia_laboral")
    private Integer idDiaLaboral;

    @Column(name = "nombre_dia", nullable = false, length = 100)
    private String nombreDia;

}

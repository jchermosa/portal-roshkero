package com.backend.portalroshkabackend.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table (name = "dias_laborales")
public class DiasLaborales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_dia_laboral")
    private Integer idDiaLaboral;

    @Column(name = "nombre_dia")
    private String nombreDia;

}

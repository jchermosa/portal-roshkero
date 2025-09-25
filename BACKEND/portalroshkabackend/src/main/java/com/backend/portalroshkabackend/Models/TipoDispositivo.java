package com.backend.portalroshkabackend.Models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tipo_dispositivo")
public class TipoDispositivo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_dispositivo")
    private Integer idTipoDispositivo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "detalle")
    private String detalle;   

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // @OneToMany(mappedBy = "tipoDispositivo", fetch = FetchType.LAZY)
    // private List<Dispositivo> dispositivos = new ArrayList<>();

}

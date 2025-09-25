package com.backend.portalroshkabackend.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
// import java.util.List;
// import java.util.ArrayList;

@Entity
@Table(name = "tipos_permisos")
@NoArgsConstructor
@Data
public class TipoPermisos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_permiso")
    private Integer idTipoPermiso;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "cant_dias")
    private Integer cantDias;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "remunerado", nullable = false)
    private Boolean remunerado = false;

    @Column(name = "fuerza_menor", nullable = false)
    private Boolean fuerzaMenor = false;

//    @OneToMany(mappedBy = "tipoPermiso", fetch = FetchType.LAZY)
//    private List<PermisosAsignados> permisosAsignados = new ArrayList<>();

}

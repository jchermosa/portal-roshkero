package com.backend.portalroshkabackend.Repositories.UsuarioRepositories;

import com.backend.portalroshkabackend.Models.AsignacionUsuarioEquipo;
import com.backend.portalroshkabackend.Models.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsigUsuarioEquipoRepository extends JpaRepository<AsignacionUsuarioEquipo, Integer> {
    List<AsignacionUsuarioEquipo> findByUsuario(Usuario usuario);
}

//Repositorio de las Asignaciones de los usuarios a los equipos

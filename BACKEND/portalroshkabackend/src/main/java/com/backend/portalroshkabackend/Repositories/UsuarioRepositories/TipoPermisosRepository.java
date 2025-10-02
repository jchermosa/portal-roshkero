package com.backend.portalroshkabackend.Repositories.UsuarioRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.TipoPermisos;

@Repository
public interface TipoPermisosRepository extends JpaRepository<TipoPermisos, Integer> {
    List<TipoPermisos> findAllByOrderByNombreAsc();
    TipoPermisos findByIdTipoPermiso(Integer idTipoPermiso);
}

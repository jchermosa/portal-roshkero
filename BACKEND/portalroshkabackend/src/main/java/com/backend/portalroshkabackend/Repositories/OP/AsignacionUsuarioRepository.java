package com.backend.portalroshkabackend.Repositories.OP;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.AsignacionUsuarioEquipo;

@Repository
public interface AsignacionUsuarioRepository extends JpaRepository<AsignacionUsuarioEquipo, Integer> {
    
    Page<AsignacionUsuarioEquipo> findByEquipo_IdEquipo(Integer idEquipo, Pageable pageable);
    List<AsignacionUsuarioEquipo> findAllByEquipo_IdEquipo(Integer idEquipo);
    Optional<AsignacionUsuarioEquipo> findByEquipo_IdEquipoAndUsuario_IdUsuario(Integer idEquipo, Integer idUsuario);
}

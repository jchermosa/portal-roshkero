package com.backend.portalroshkabackend.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.AsignacionUsuario;
import com.backend.portalroshkabackend.Models.Usuario;

@Repository
public interface AsignacionUsuarioRepository extends JpaRepository<AsignacionUsuario, Integer> {
    Page<AsignacionUsuario> findByEquipo_IdEquipo(Integer idEquipo, Pageable pageable);

    List<AsignacionUsuario> findAllByEquipo_IdEquipo(Integer idEquipo);
    // boolean existsByIdUsuarioAndEquiposIdEquipo(Integer idUsuario, Integer
    // idEquipo);
}

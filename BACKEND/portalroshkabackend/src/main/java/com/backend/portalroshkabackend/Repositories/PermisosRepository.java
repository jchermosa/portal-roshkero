package com.backend.portalroshkabackend.Repositories;

import com.backend.portalroshkabackend.Models.TipoPermisos;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermisosRepository extends JpaRepository<TipoPermisos, Integer> {
}

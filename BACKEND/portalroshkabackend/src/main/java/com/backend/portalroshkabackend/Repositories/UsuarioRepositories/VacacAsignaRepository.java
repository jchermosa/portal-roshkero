package com.backend.portalroshkabackend.Repositories.UsuarioRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.VacacionesAsignadas;

@Repository
public interface VacacAsignaRepository extends JpaRepository<VacacionesAsignadas, Integer> {
    // List<VacacionesAsignadas> findByIdUsuario(Integer idUsuario);
    
}

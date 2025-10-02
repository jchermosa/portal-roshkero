package com.backend.portalroshkabackend.Repositories.TH;

import com.backend.portalroshkabackend.Models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

    Boolean existsByNombre(String nombre);
    Boolean existsByNombreAndIdRolNot(String nombre, Integer idRol);
}

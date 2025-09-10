package com.backend.portalroshkabackend.admin.repository;

import com.backend.portalroshkabackend.common.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
}

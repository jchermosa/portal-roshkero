package com.backend.portalroshkabackend.admin.repository;

import com.backend.portalroshkabackend.common.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
}

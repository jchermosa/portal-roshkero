package com.backend.portalroshkabackend.admin.repository;


import com.backend.portalroshkabackend.common.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Usuario, Integer> {
}

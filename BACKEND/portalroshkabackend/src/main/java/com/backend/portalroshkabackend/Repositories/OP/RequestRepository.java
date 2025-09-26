package com.backend.portalroshkabackend.Repositories.OP;

import com.backend.portalroshkabackend.Models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Solicitud, Integer> {
}

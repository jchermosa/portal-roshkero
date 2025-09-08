package com.backend.portalroshkabackend.admin.repository;

import com.backend.portalroshkabackend.common.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}

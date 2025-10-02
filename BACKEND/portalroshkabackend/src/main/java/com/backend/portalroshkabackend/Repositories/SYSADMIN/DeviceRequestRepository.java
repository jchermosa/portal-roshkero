package com.backend.portalroshkabackend.Repositories.SYSADMIN;

import com.backend.portalroshkabackend.Models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRequestRepository extends JpaRepository<Solicitud, Integer> {

}

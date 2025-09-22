package com.backend.portalroshkabackend.Repositories;

import com.backend.portalroshkabackend.Models.SolicitudDispositivos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRequestRepository extends JpaRepository<SolicitudDispositivos, Integer> {

}

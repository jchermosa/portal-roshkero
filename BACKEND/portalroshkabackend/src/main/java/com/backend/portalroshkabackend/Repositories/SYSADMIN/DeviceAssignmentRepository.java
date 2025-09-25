package com.backend.portalroshkabackend.Repositories.SYSADMIN;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.DispositivoAsignado;
import java.util.List;


@Repository
public interface DeviceAssignmentRepository extends JpaRepository<DispositivoAsignado, Integer> {


   List<DispositivoAsignado> findAll();

}
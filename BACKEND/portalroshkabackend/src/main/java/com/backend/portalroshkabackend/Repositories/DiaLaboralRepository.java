package com.backend.portalroshkabackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.DiaLaboral;

@Repository
public interface DiaLaboralRepository extends JpaRepository<DiaLaboral, Integer> {

}

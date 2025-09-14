package com.backend.portalroshkabackend.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.Models.SolicitudDispositivos;
import com.backend.portalroshkabackend.Repositories.SysAdminRepository;

@Service
public class SysAdminService {

    @Autowired
    private SysAdminRepository sysAdminRepository;


    SysAdminService(SysAdminRepository sysAdminRepository) {
        this.sysAdminRepository = sysAdminRepository;
    }


    public List<SolicitudDispositivos> getAllAprovedRequest(){
        return sysAdminRepository.findSolicitudesAprovadas();
    }

    public List<SolicitudDispositivos> findAllSolicitudes(){
        return sysAdminRepository.findAllSolicitudes();
    }



}

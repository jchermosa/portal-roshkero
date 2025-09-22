package com.backend.portalroshkabackend.Services.SysAdmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.portalroshkabackend.Models.SolicitudDispositivos;
import com.backend.portalroshkabackend.Repositories.SysAdminRepository;
import com.backend.portalroshkabackend.Services.DispositivoService;

@Service
public class SysAdminService {

    @Autowired
    private SysAdminRepository sysAdminRepository;

    @Autowired
    private final DispositivoService dispositivoService;


    SysAdminService(SysAdminRepository sysAdminRepository, DispositivoService dispositivoService) {
        this.sysAdminRepository = sysAdminRepository;
        this.dispositivoService = dispositivoService;
    }


    @Transactional(readOnly = true)
    public List<SolicitudDispositivos> getAllRejectedRequest(){
        return sysAdminRepository.findSolicitudesRechazadas();
    }

    @Transactional(readOnly = true)
    public List<SolicitudDispositivos> getAllApprovedRequest(){
        return sysAdminRepository.findSolicitudesAprobadas();
    }

    @Transactional (readOnly = true)
    public List<SolicitudDispositivos> getAllPendingRequest(){
        return sysAdminRepository.findSolicitudesPendientes();
    }

    @Transactional(readOnly = true)
    public List<SolicitudDispositivos> findAllSolicitudes(){
        return sysAdminRepository.findAllSolicitudes();
    }

    @Transactional
    public void deleteDispositivo(Integer id){

        dispositivoService.deleteDeviceById(id);
    }

    

}

package com.backend.portalroshkabackend.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.DTO.DispositivoDto;
import com.backend.portalroshkabackend.Models.Dispositivo;
import com.backend.portalroshkabackend.Repositories.DispositivoRepository;


@Service
public class DispositivoService {
    


    @Autowired
    DispositivoRepository dispositivoRepository;

    public DispositivoService(DispositivoRepository dispositivoRepository) {
        this.dispositivoRepository = dispositivoRepository;
    }

    // insert tipo de inventario
    @Transactional
    public Dispositivo createDevice(DispositivoDto dispositivo) {

        // creando un nuevo dispositivo
        Dispositivo newDispositivo = new Dispositivo();
        newDispositivo.setNombre(dispositivo.getNombre());
        newDispositivo.setDetalle(dispositivo.getDetalle());

        return dispositivoRepository.save(newDispositivo);
    }

    @Transactional(readOnly = true)
    public List<Dispositivo> getAllDevices() {
        return dispositivoRepository.findAll();
    }

    @Transactional
    public Dispositivo updateDevice(Integer id, DispositivoDto dispositivoDto) {
        Dispositivo existingDispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo not found with id: " + id));

        existingDispositivo.setNombre(dispositivoDto.getNombre());
        existingDispositivo.setDetalle(dispositivoDto.getDetalle());

        return dispositivoRepository.save(existingDispositivo);
    }

    @Transactional
    public void deleteDeviceById(Integer id) {
        dispositivoRepository.deleteById(id);
    }

}

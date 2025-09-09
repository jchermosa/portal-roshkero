package com.backend.portalroshkabackend.Service;


import com.backend.portalroshkabackend.Models.usuarios;
import com.backend.portalroshkabackend.Repository.usuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class usuariosService {
    @Autowired

    usuariosRepository usuariosRepository;

    public List<usuarios> getUsuarios(){
        return (List<usuarios>) usuariosRepository.findAll();
    }

    public Optional<usuarios> getUsuarios(Integer id){
        return usuariosRepository.findById(id);
    }

    public void saveUsuarios(usuarios usuarios){
        usuariosRepository.save(usuarios);
    }

    public void delete(Integer id){
        usuariosRepository.deleteById(id);
    }
}

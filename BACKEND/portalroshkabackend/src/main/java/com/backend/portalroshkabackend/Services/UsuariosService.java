package com.backend.portalroshkabackend.Services;


import com.backend.portalroshkabackend.Models.Usuarios;
import com.backend.portalroshkabackend.Repositories.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuariosService {
    @Autowired
    UsuariosRepository usuariosRepository;

    public List<Usuarios> getUsuarios(){
        return (List<Usuarios>) usuariosRepository.findAll();
    }

    public Optional<Usuarios> getUsuarios(Integer id){
        return usuariosRepository.findById(id);
    }

    public void saveUsuarios(Usuarios usuarios){
        usuariosRepository.save(usuarios);
    }

    public void delete(Integer id){
        usuariosRepository.deleteById(id);
    }
    
    public Usuarios getUserByCorreo(String correo) {
        Optional<Usuarios> usuario = usuariosRepository.findByCorreo(correo);
        return usuario.orElse(null);
    }
}

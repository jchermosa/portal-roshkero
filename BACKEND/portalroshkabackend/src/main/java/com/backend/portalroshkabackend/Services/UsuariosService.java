package com.backend.portalroshkabackend.Services;


import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuariosService {
    @Autowired
    UserRepository UsuarioRepository;

    public List<Usuario> getUsuario(){
        return (List<Usuario>) UsuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuario(Integer id){
        return UsuarioRepository.findById(id);
    }

    public void saveUsuario(Usuario Usuario){
        UsuarioRepository.save(Usuario);
    }

    public void delete(Integer id){
        UsuarioRepository.deleteById(id);
    }
    
    public Usuario getUserByCorreo(String correo) {
        Optional<Usuario> usuario = UsuarioRepository.findByCorreo(correo);
        return usuario.orElse(null);
    }
}

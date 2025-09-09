// package com.backend.portalroshkabackend.Services;
// import java.util.Collection;
// import java.util.stream.Collectors;
// import java.util.stream.StreamSupport;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.backend.portalroshkabackend.Models.Usuarios;
// import com.backend.portalroshkabackend.Models.Roles;
// import com.backend.portalroshkabackend.Repositories.UserRepository;

// @Service
// public class JpaUserDetailsService implements UserDetailsService {

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private RolesService rolesService;

//     @Override
//     @Transactional
//     public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
//         Usuarios user = userRepository.findByCorreo(correo);

//         if (user == null) {
//             throw new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo);
//         }

//         // Si quieres listar todos los roles de la BD (opcional)
//         Iterable<Roles> todosRoles = rolesService.getAllRoles();

//         // Mapeamos los roles del usuario a GrantedAuthority
//         Collection<? extends GrantedAuthority> authorities = 
//             StreamSupport.stream(todosRoles.spliterator(), false)  // false → stream secuencial
//                 .filter(r -> r.getIdRol().equals(user.getIdRol())) // si el usuario tiene ese rol
//                 .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getNombre()))
//                 .collect(Collectors.toList());

//         // Devolvemos un objeto que Spring Security pueda manejar
//         return new org.springframework.security.core.userdetails.User(
//                 user.getCorreo(),       // username → será el correo
//                 user.getContrasena(),   // contraseña encriptada en la BD
//                 authorities             // lista de roles/permisos
//         );
//     }
// }

package com.backend.portalroshkabackend.Security.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.backend.portalroshkabackend.Models.Usuarios;
import com.backend.portalroshkabackend.Services.UsuariosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.backend.portalroshkabackend.Security.TokenJwtConfig.*;



public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    
    private final UsuariosService userService;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UsuariosService userService) {
        super.setAuthenticationManager(authenticationManager);
        this.userService = userService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            Usuarios user = new ObjectMapper().readValue(request.getInputStream(), Usuarios.class);

            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(user.getCorreo(), user.getContrasena());

            setDetails(request, authRequest); // importante para que el filtro padre agregue metadata
            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
            User user = (User) authResult.getPrincipal();
            String correo = user.getUsername();

            // obtenemos el usuario del correo para obtener su rol
            Usuarios usuario = userService.getUserByCorreo(correo);

            String token = Jwts.builder()
                .subject(correo)
                .claim("rol", usuario.getIdRol())
                .expiration(Date.from(new Date().toInstant().plusSeconds(7200))) // 2 horas de validez
                .issuedAt(Date.from(new Date().toInstant()))
                .signWith(SECRET_KEY)
                .compact();

            response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

            Map<String, String> body = new HashMap<>();
            body.put("token", token);
            body.put("correo", correo);
            body.put("rol", String.valueOf(usuario.getIdRol()));
            body.put("Message", "Authentication successful");

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);

    }

    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        Map<String, String> body = new HashMap<>();
        body.put("Message", "Authentication failed");
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}

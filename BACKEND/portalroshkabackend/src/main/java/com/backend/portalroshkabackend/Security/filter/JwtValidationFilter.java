package com.backend.portalroshkabackend.Security.filter;

import static com.backend.portalroshkabackend.Security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.backend.portalroshkabackend.Security.TokenJwtConfig.PREFIX_TOKEN;
import static com.backend.portalroshkabackend.Security.TokenJwtConfig.SECRET_KEY;

import java.io.IOException;
import java.security.Security;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter{
    
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        

                String header = request.getHeader(HEADER_AUTHORIZATION);
                if(header == null || !header.startsWith(PREFIX_TOKEN)){
                    chain.doFilter(request, response);
                    return;
                }

                
                String token = header.replace(PREFIX_TOKEN, "");
                
                // comenzamos a validar los claims  
                
                try {
                    Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
                    String correo = claims.getSubject();
                    Integer rol = claims.get("rol", Integer.class);

                    Collection<? extends GrantedAuthority> authorities = 
                    List.of(new SimpleGrantedAuthority("ROLE_" + rol));

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(correo, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    chain.doFilter(request, response);

                } catch (JwtException e) {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");

                    response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(error));
                }
    }
}

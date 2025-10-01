package com.backend.portalroshkabackend.Security.filter;

import static com.backend.portalroshkabackend.Security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.backend.portalroshkabackend.Security.TokenJwtConfig.PREFIX_TOKEN;
import static com.backend.portalroshkabackend.Security.TokenJwtConfig.SECRET_KEY;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        
        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(PREFIX_TOKEN, "");

        try {
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            String correo = claims.getSubject();
            Object roleClaim = claims.get("rol");


            if (correo != null && roleClaim != null) {
                // Convertir el rol a entero
                Integer rolId = null;
                if (roleClaim instanceof Integer) {
                    rolId = (Integer) roleClaim;
                } else if (roleClaim instanceof Number) {
                    rolId = ((Number) roleClaim).intValue();
                } else {
                    rolId = Integer.valueOf(roleClaim.toString());
                }

                // Crear la autoridad con el formato correcto
                String authority = "ROLE_" + rolId;
                Collection<GrantedAuthority> authorities = Arrays.asList(
                    new SimpleGrantedAuthority(authority)
                );
 

                UsernamePasswordAuthenticationToken auth = 
                    new UsernamePasswordAuthenticationToken(correo, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            chain.doFilter(request, response);

        } catch (JwtException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token inválido");
            error.put("message", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
        }
    }
}

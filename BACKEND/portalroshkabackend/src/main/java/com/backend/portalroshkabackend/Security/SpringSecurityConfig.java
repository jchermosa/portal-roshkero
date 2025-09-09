package com.backend.portalroshkabackend.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.backend.portalroshkabackend.Security.filter.JwtAuthenticationFilter;

// Bean de condiguracion de seguridad


@Configuration
public class SpringSecurityConfig {


    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;


    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
            jwtAuthenticationFilter.setFilterProcessesUrl("/login");
            http
                .authorizeHttpRequests(auth -> 
                    auth.requestMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilter(jwtAuthenticationFilter)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> 
                    session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
                );
            return http.build();
    }




}

package com.backend.portalroshkabackend.security.controller;

import com.backend.portalroshkabackend.common.DTO.LoginDto;
import com.backend.portalroshkabackend.common.DTO.ResponseLoginDto;
import com.backend.portalroshkabackend.security.dto.JwtResponse;
import com.backend.portalroshkabackend.security.service.JwtUserDetailsService;
import com.backend.portalroshkabackend.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDto authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getCorreo(), authenticationRequest.getContrasena());
        
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getCorreo());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new ResponseLoginDto(token, authenticationRequest.getCorreo()));
    }

    private void authenticate(String username, String password) throws Exception {
        System.out.println("Username: " + username + ", Password: " + password);
        try {
            System.out.println("ENTRANDO");
            // verifica si matchea con la bd
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

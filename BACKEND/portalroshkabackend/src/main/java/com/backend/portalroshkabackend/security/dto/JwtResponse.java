package com.backend.portalroshkabackend.security.dto;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String token;
    private final String username;

    public JwtResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return this.token;
    }
    
    public String getUsername() {
        return this.username;
    }
}

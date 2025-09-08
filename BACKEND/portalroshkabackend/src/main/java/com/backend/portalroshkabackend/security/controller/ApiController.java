package com.backend.portalroshkabackend.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    // Public endpoint - accessible without authentication
    @GetMapping("/public/hello")
    public ResponseEntity<?> publicHello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from public API!");
        return ResponseEntity.ok(response);
    }

    // Protected endpoint - requires authentication
    @GetMapping("/protected/hello")
    public ResponseEntity<?> protectedHello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from protected API! You are authenticated.");
        return ResponseEntity.ok(response);
    }
}

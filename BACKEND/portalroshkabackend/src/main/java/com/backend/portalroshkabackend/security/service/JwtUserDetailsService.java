package com.backend.portalroshkabackend.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    // TODO: Replace this with actual user service implementation
    // This is a placeholder implementation that returns a hardcoded user
    // In a real application, you would look up the user from your database
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // For demonstration purposes only
        if ("admin@roshka.com".equals(username)) {
            // Use this hardcoded user for now - in a real app, lookup from database
            return new User("admin@roshka.com", 
                "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", 
                new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}

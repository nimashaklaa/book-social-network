package com.ami.book_net.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The master control switch for security in the application.
 * Instead of hitting the Controller immediately, every single incoming HTTP request has to walk down a long hallway filled with guards standing in a line. Each guard is a Filter, and together they make up the FilterChain.
 */

@Configuration //take the object it returns, and manage it globally
@EnableWebSecurity // deactivate the default security configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true) //This activates role-based security directly on top of your Controller methods using Java annotations.

public class SecurityConfig {
    @Bean // The @Bean annotation is the bridge that connects your custom code to Spring’s engine.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return null;
    }
}

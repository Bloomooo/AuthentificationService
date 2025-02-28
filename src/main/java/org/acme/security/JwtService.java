package org.acme.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtService {

    @ConfigProperty(name = "JWT_SECRET")
    String SECRET;

    public  JwtService() {
    }

    public String generateToken(String username) {
        return Jwt.upn(username).signWithSecret(SECRET);
    }

    public String extractUsername(String token) {
        return "";
    }
}
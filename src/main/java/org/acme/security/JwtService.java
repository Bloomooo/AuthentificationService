package org.acme.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtService {

    public String generateToken(String username, String role) {
        Set<String> roles = new HashSet<>();
        roles.add(role);

        return Jwt.issuer("http://localhost")
                .upn(username)
                .groups(roles)
                .expiresAt(System.currentTimeMillis() / 1000 + 3600) // Expire en 1 heure
                .sign();
    }
}

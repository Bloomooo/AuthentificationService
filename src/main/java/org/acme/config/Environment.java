package org.acme.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Environment {

    @ConfigProperty(name = "PERSONAL_SALT")
    String PERSONAL_SALT;

    @ConfigProperty(name = "JWT_SECRET")
    String SECRET;

    public String getPersonalSalt() {
        return PERSONAL_SALT;
    }

    public String getSecret() {
        return SECRET;
    }
}

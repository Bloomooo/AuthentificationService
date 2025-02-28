package org.acme.service.create;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.repository.IUserRepository;
import org.acme.security.JwtService;

@ApplicationScoped
public class CForgotPasswordUserService {
    private final IUserRepository userRepository;
    private final JwtService jwtService;

    public CForgotPasswordUserService(IUserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }
}

package org.acme.service.create;

import org.acme.config.Environment;
import org.acme.model.User;
import org.acme.model.type.Role;
import org.acme.repository.IUserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CCreateUserService {

    private final IUserRepository userRepository;
    private final Environment environment;
    private final Logger logger = LoggerFactory.getLogger(CCreateUserService.class);

    public CCreateUserService(IUserRepository userRepository, Environment environment) {
        this.userRepository = userRepository;
        this.environment = environment;
    }

    @WithSession
    public Uni<User> createUser(User user) {
        user.setRole(Role.USER);
        String hashedPassword = BCrypt.hashpw(user.getPassword(), this.environment.getPersonalSalt());
        user.setPassword(hashedPassword);
        return Uni.createFrom().item(user)
                .onFailure().invoke(e -> this.logger.error("Error while creating user", e))
                .onItem().transformToUni(u -> this.userRepository.persistAndFlush(user));
    }
}

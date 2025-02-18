package org.acme.service.create;

import org.acme.model.User;
import org.acme.model.type.Role;
import org.acme.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CCreateUserService {

    private final Logger logger = LoggerFactory.getLogger(CCreateUserService.class);

    @WithSession
    public Uni<User> createUser(User user, IUserRepository userRepository) {
        user.setRole(Role.USER);
        String hashedPassword = BcryptUtil.bcryptHash(user.getPassword());
        user.setPassword(hashedPassword);
        return Uni.createFrom().item(user)
                .onFailure().invoke(e -> this.logger.error("Error while creating user", e))
                .onItem().transformToUni(u -> userRepository.persistAndFlush(user));
    }
}

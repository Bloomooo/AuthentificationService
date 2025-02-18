package org.acme.service;

import java.util.List;

import org.acme.model.User;
import org.acme.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CUserService {

    private final IUserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(CUserService.class);

    public CUserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @WithSession
    public Uni<List<User>> getAllUsers() {
        return this.userRepository.findAll().list();
    }

    @WithSession
    public Uni<User> createUser(User user) {
        return Uni.createFrom().item(user)
                .onFailure().invoke(e -> logger.error("Error while creating user", e))
                .onItem().transformToUni(u -> userRepository.persistAndFlush(user));
    }
}

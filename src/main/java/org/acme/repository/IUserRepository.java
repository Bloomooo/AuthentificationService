package org.acme.repository;

import org.acme.model.User;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IUserRepository implements PanacheRepository<User> {
}

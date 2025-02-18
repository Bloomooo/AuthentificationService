package org.acme.repository;

import org.acme.model.User;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IUserRepository implements PanacheRepository<User> {

    public Uni<User> findByUsername(String username){
        return find("username", username).firstResult();
    }

    public Uni<User> findByMail(String mail){
        return find("mail", mail).firstResult();
    }

    @WithSession
    public Uni<Boolean> isUsernameAlreadyExists(String username){
        return find("username", username).firstResult().map(user -> user != null);
    }

    @WithSession
    public Uni<Boolean> isMailAlreadyExists(String mail){
        return find("mail", mail).firstResult().map(user -> user != null);
    }
}

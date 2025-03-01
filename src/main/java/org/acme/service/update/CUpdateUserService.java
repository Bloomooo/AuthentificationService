package org.acme.service.update;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.User;
import org.acme.repository.IUserRepository;

import java.util.Objects;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CUpdateUserService {
    private final IUserRepository userRepository;
    private static final Logger LOG = Logger.getLogger(CUpdateUserService.class);

    public CUpdateUserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @WithSession
    public Uni<Boolean> deleteUser(Long id) {
        return this.userRepository.deleteById(id)
                .onItem().transform(deleted -> {
                    LOG.info("User deleted: " + deleted);
                    return deleted;
                })
                .onFailure().recoverWithItem(throwable -> {
                    LOG.error("Failed to delete user", throwable);
                    return false;
                });
    }

    @WithSession
    public Uni<Boolean> updateUser(User user){
        return this.userRepository.findById(user.getId())
                .onItem().transformToUni(existingUser -> {
                    if (existingUser != null) {
                        existingUser.setUsername(user.getUsername());
                        existingUser.setMail(user.getMail());
                        existingUser.setPassword(user.getPassword());
                        return this.userRepository.persistAndFlush(existingUser)
                                .onItem().transform(updatedUser -> {
                                    LOG.info("User updated: " + updatedUser);
                                    return updatedUser != null;
                                });
                    } else {
                        LOG.warn("User not found for update: " + user.getId());
                        return Uni.createFrom().item(false);
                    }
                })
                .onFailure().recoverWithItem(throwable -> {
                    LOG.error("Failed to update user", throwable);
                    return false;
                });
    }
}

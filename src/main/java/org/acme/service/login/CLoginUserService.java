package org.acme.service.login;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import org.acme.dto.http.CLoginUser;
import org.acme.model.User;
import org.acme.repository.IUserRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.elytron.security.common.BcryptUtil;

@ApplicationScoped
public class CLoginUserService {

    private final IUserRepository userRepository;

    public CLoginUserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @WithSession
    public Uni<CLoginUser.Output> loginUser(CLoginUser.Input input) {
        return this.userRepository.findByUsername(input.username)
                .onItem().transform(user -> {
                    CLoginUser.Output output = new CLoginUser.Output();
                    if (user == null) {
                        output.message = "User not found";
                        output.success = false;
                    }
                    if (user != null && !BcryptUtil.matches(input.password, user.getPassword())) {
                        output.message = "Invalid password";
                        output.success = false;
                    }
                    output.user = user;
                    output.success = true;
                    output.message = "Connexion réussie";
                    return output;
                });
    }
}

package org.acme.service.login;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import org.acme.dto.http.CLoginUser;
import org.acme.repository.IUserRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.security.JwtService;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class CLoginUserService {

    private final IUserRepository userRepository;
    private final JwtService jwtService;
    public CLoginUserService(IUserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @WithSession
    public Uni<CLoginUser.Output> loginUser(CLoginUser.Input input) {
        return this.userRepository.findByUsername(input.username)
                .onItem().transformToUni(user -> {
                    CLoginUser.Output output = new CLoginUser.Output();
                    if (user == null) {
                        output.message = "User not found";
                        output.success = false;
                        return Uni.createFrom().item(output);
                    }
                    if (!BCrypt.checkpw(input.password, user.getPassword())) {
                        output.message = "Invalid password";
                        output.success = false;
                        return Uni.createFrom().item(output);
                    }
                    String token = this.jwtService.generateToken(user.getUsername());
                    user.setToken(token);
                    return this.userRepository.persistAndFlush(user)
                            .onItem().transform(v -> {
                                output.token = token;
                                output.success = true;
                                output.message = "Connexion réussie";
                                return output;
                            });
                });
    }
}

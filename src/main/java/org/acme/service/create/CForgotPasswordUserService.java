package org.acme.service.create;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.config.Environment;
import org.acme.dto.http.CForgotPassword;
import org.acme.repository.IUserRepository;
import org.acme.security.JwtService;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class CForgotPasswordUserService {
    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final Environment environment;

    public CForgotPasswordUserService(IUserRepository userRepository, JwtService jwtService, Environment environment) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.environment = environment;
    }

    @WithSession
    public Uni<CForgotPassword.Output> forgotPassword(CForgotPassword.Input input) {
        String email = jwtService.extractUsername(input.token);
        return this.userRepository.findByMail(email)
                .onItem().transformToUni(user -> {
                    CForgotPassword.Output output = new CForgotPassword.Output();
                    if (user == null) {
                        output.message = "User not found";
                        output.isSuccess = false;
                        return Uni.createFrom().item(output);
                    }
                    user.setPassword(BCrypt.hashpw(input.password, this.environment.getPersonalSalt()));
                    return this.userRepository.persistAndFlush(user)
                            .onItem().transform(v -> {
                                output.isSuccess = true;
                                output.message = "Password changed successfully";
                                return output;
                            });
                });
    }

}

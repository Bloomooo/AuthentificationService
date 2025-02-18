package org.acme.service;

import java.util.List;

import org.acme.dto.http.CCreateUser;
import org.acme.model.User;
import org.acme.repository.IUserRepository;
import org.acme.service.common.CUserCommonService;
import org.acme.service.create.CCreateUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CUserService {

    private final IUserRepository userRepository;
    private final CCreateUserService createUserService;
    private final CUserCommonService commonService;
    private final Logger logger = LoggerFactory.getLogger(CUserService.class);

    public CUserService(IUserRepository userRepository, CCreateUserService createUserService,
            CUserCommonService commonService) {
        this.userRepository = userRepository;
        this.createUserService = createUserService;
        this.commonService = commonService;
    }

    @WithSession
    public Uni<List<User>> getAllUsers() {
        return this.userRepository.findAll().list();
    }

    public Uni<CCreateUser.Output> createUser(CCreateUser.Input input) {
        CCreateUser.Output output = new CCreateUser.Output();
        User user = new User();
        user.setUsername(input.username);
        user.setMail(input.mail);
        user.setPassword(input.password);
    
        if (!this.commonService.isUserValid(user)) {
            output.message = "Le nom d'utilisateur, le mail ou le mot de passe est invalide";
            output.success = false;
            return Uni.createFrom().item(output);
        }
    
        return this.commonService.isUsernameAlreadyExists(user.getUsername(), this.userRepository)
            .onItem().transformToUni(exist -> {
                if (exist) {
                    output.message = "L'utilisateur existe déjà";
                    output.success = false;
                    return Uni.createFrom().item(output);
                }
                return this.commonService.isMailAlreadyExists(user.getMail(), this.userRepository)
                    .onItem().transformToUni(mailExist -> {
                        if (mailExist) {
                            output.message = "Le mail existe déjà";
                            output.success = false;
                            return Uni.createFrom().item(output);
                        }
                        if (!this.commonService.isPasswordMatch(user.getPassword())) {
                            output.message = "Le mot de passe doit contenir au moins 8 caractères, une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial";
                            output.success = false;
                            return Uni.createFrom().item(output);
                        }
                        return this.createUserService.createUser(user, this.userRepository)
                            .onItem().transform(createdUser -> {
                                output.user = createdUser;
                                output.message = "Utilisateur créé avec succès";
                                output.success = true;
                                return output;
                            });
                    });
            })
            .onFailure().recoverWithItem(e -> {
                this.logger.error("Error while creating user", e);
                output.message = "Erreur lors de la création de l'utilisateur";
                output.success = false;
                return output;
            });
    }    
}

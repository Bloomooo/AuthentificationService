package org.acme.service;

import java.util.List;

import org.acme.dto.http.*;
import org.acme.model.User;
import org.acme.repository.IUserRepository;
import org.acme.service.common.CUserCommonService;
import org.acme.service.create.CCreateUserService;
import org.acme.service.create.CForgotPasswordUserService;
import org.acme.service.login.CLoginUserService;
import org.acme.service.update.CUpdateUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CUserService {
    private final CCreateUserService createUserService;
    private final CUserCommonService commonService;
    private final CLoginUserService loginUserService;
    private final CForgotPasswordUserService forgotPasswordUserService;
    private final CUpdateUserService updateUserService;
    private final Logger logger = LoggerFactory.getLogger(CUserService.class);

    public CUserService(IUserRepository userRepository, CCreateUserService createUserService,
            CUserCommonService commonService, CLoginUserService loginUserService, CForgotPasswordUserService forgotPasswordUserService, CUpdateUserService updateUserService) {
        this.createUserService = createUserService;
        this.commonService = commonService;
        this.loginUserService = loginUserService;
        this.forgotPasswordUserService = forgotPasswordUserService;
        this.updateUserService = updateUserService;
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

        return this.commonService.isUsernameAlreadyExists(user.getUsername())
            .onItem().transformToUni(exist -> {
                if (exist) {
                    output.message = "L'utilisateur existe déjà";
                    output.success = false;
                    return Uni.createFrom().item(output);
                }
                return this.commonService.isMailAlreadyExists(user.getMail())
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
                        return this.createUserService.createUser(user)
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

    public Uni<CLoginUser.Output> loginUser(CLoginUser.Input input) {
        return this.loginUserService.loginUser(input)
            .onItem().transform(out -> {
                CLoginUser.Output output = new CLoginUser.Output();
                output.token = out.token;
                output.message = out.message;
                output.success = out.success;
                return output;
            })
            .onFailure().recoverWithItem(e -> {
                this.logger.error("Error while login user", e);
                CLoginUser.Output output = new CLoginUser.Output();
                output.message = "Erreur lors de la connexion";
                output.success = false;
                return output;
            });
    }

    public Uni<CForgotPassword.Output> forgotPassword(CForgotPassword.Input input) {
        return this.forgotPasswordUserService.forgotPassword(input)
                .onItem().transform(out -> {
                   CForgotPassword.Output output = new CForgotPassword.Output();
                     output.message = out.message;
                        output.isSuccess = out.isSuccess;
                        return output;
                })
                .onFailure().recoverWithItem(e -> {
                    CForgotPassword.Output output = new CForgotPassword.Output();
                    output.message = "Erreur lors de la récupération du mot de passe";
                    output.isSuccess = false;
                    return output;
                });
    }

    public Uni<CDeleteUser.Output> deleteUser(CDeleteUser.Input input) {
        return this.updateUserService.deleteUser(input.id)
            .onItem().transform(success -> {
                CDeleteUser.Output output = new CDeleteUser.Output();
                if(success){
                    output.message = "Utilisateur supprimé avec succès";
                    output.isSuccess = true;
                }else {
                    output.message = "Erreur lors de la suppression de l'utilisateur";
                    output.isSuccess = false;
                }
                return output;
            })
            .onFailure().recoverWithItem(e -> {
                this.logger.error("Error while deleting user"+e);
                CDeleteUser.Output output = new CDeleteUser.Output();
                output.message = "Erreur lors de la suppression de l'utilisateur";
                output.isSuccess = false;
                return output;
            });
    }

    public Uni<CEditUser.Output> updateUser(CEditUser.Input input) {
        return this.updateUserService.updateUser(input.user)
                .onItem().transform(success ->{
                    CEditUser.Output output = new CEditUser.Output();
                    if(success){
                        output.message = "Utilisateur modifié avec succès";
                        output.isSuccess = true;
                    }else {
                        output.message = "Erreur lors de la modification de l'utilisateur";
                        output.isSuccess = false;
                    }
                    return output;
                })
                .onFailure().recoverWithItem(e ->{
                    this.logger.error("Error while updating user"+e);
                    CEditUser.Output output = new CEditUser.Output();
                    output.message = "Erreur lors de la modification de l'utilisateur";
                    output.isSuccess = false;
                    return output;
                });
    }

    public Uni<CGetAllUsers.Output> getAllUsers(CGetAllUsers.Input input) {
        return this.commonService.getAllUsers()
            .onItem().transform(users -> {
                CGetAllUsers.Output output = new CGetAllUsers.Output();
                output.users = users;
                return output;
            })
            .onFailure().recoverWithItem(e -> {
                this.logger.error("Error while getting all users", e);
                CGetAllUsers.Output output = new CGetAllUsers.Output();
                output.message = "Erreur lors de la récupération des utilisateurs";
                return output;
            });
    }

    public Uni<CFilterUsers.Output> filterUsers(CFilterUsers.Input input) {
        return this.commonService.filterUsers(input.name)
            .onItem().transform(users -> {
                CFilterUsers.Output output = new CFilterUsers.Output();
                output.users = users;
                output.isSuccess = true;
                output.message = "Utilisateurs récupérés avec succès";
                return output;
            })
            .onFailure().recoverWithItem(e -> {
                this.logger.error("Error while filtering users", e);
                CFilterUsers.Output output = new CFilterUsers.Output();
                output.message = "Erreur lors de la récupération des utilisateurs";
                output.isSuccess = false;
                return output;
            });
    }
}


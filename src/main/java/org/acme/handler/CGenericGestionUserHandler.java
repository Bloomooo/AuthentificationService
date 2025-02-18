package org.acme.handler;

import org.acme.dto.CDTOGestionUser;
import org.acme.dto.http.CCreateUser;
import org.acme.model.User;
import org.acme.service.CUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CGenericGestionUserHandler implements CDTOGestionUser.IHandlerDTOGestionUser {

    private final CUserService userService;
    private final Logger logger = LoggerFactory.getLogger(CGenericGestionUserHandler.class);

    @Inject
    public CGenericGestionUserHandler(CUserService userService) {
        this.userService = userService;
    }

    @Override
    public Uni<CCreateUser.Output> createUser(CCreateUser.Input input) {
        User user = new User();
        user.name = input.name;
        user.email = input.email;
        user.password = input.password;
        return this.userService.createUser(user)
                .onItem().transform(createdUser -> {
                    CCreateUser.Output output = new CCreateUser.Output();
                    output.user = createdUser;
                    output.message = "User created successfully";
                    output.success = true;
                    return output;
                })
                .onFailure().recoverWithItem(e -> {
                    this.logger.error("Error creating user", e);
                    CCreateUser.Output output = new CCreateUser.Output();
                    output.message = "Error creating user";
                    output.success = false;
                    return output;
                });
    }
}

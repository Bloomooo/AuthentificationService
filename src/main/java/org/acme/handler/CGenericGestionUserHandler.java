package org.acme.handler;

import org.acme.dto.CDTOGestionUser;
import org.acme.dto.http.CCreateUser;
import org.acme.dto.http.CCreateUser.Output;
import org.acme.service.CUserService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CGenericGestionUserHandler implements CDTOGestionUser.IHandlerDTOGestionUser {

    private final CUserService userService;

    @Inject
    public CGenericGestionUserHandler(CUserService userService) {
        this.userService = userService;
    }

    @Override
    public Uni<CCreateUser.Output> createUser(CCreateUser.Input input) {
        return (Uni<Output>) this.userService.createUser(input).onItem().transform(output -> {
            return output;
        });
    }
}

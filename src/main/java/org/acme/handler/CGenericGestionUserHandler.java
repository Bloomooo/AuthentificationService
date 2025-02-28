package org.acme.handler;

import org.acme.dto.CDTOGestionUser;
import org.acme.dto.http.CCreateUser;
import org.acme.dto.http.CCreateUser.Output;
import org.acme.dto.http.CForgotPassword;
import org.acme.dto.http.CLoginUser;
import org.acme.service.CUserService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        return (Uni<CCreateUser.Output>) this.userService.createUser(input).onItem().transform(output -> {
            return output;
        });
    }

    @Override
    public Uni<CLoginUser.Output> loginUser(CLoginUser.Input input) {
        try{
            return (Uni<CLoginUser.Output>) this.userService.loginUser(input).onItem().transform(out -> {
                return out;
            });
        } catch (Exception e) {
            CLoginUser.Output output = new CLoginUser.Output();
            output.message = e.getMessage();
            output.success = false;
            this.logger.error(e.getMessage());
            return Uni.createFrom().item(output);
        }
    }

    @Override
    public Uni<CForgotPassword.Output> forgotPassword(CForgotPassword.Input input) {
        CForgotPassword.Output output = new CForgotPassword.Output();
        try{

        }catch(Exception e){
            this.logger.error(e.getMessage());
            return Uni.createFrom().item(output);
        }
        return Uni.createFrom().item(output);
    }
}

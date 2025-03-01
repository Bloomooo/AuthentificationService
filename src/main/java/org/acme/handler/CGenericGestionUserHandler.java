package org.acme.handler;

import org.acme.dto.CDTOGestionUser;
import org.acme.dto.http.*;
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
        return this.userService.createUser(input).onItem().transform(output -> output);
    }

    @Override
    public Uni<CLoginUser.Output> loginUser(CLoginUser.Input input) {
        try{
            return this.userService.loginUser(input).onItem().transform(out -> out);
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
            return this.userService.forgotPassword(input).onItem().transform(out -> out);
        }catch(Exception e){
            this.logger.error(e.getMessage());
            return Uni.createFrom().item(output);
        }
    }

    /**
     * @param input 
     * @return
     */
    @Override
    public Uni<CDeleteUser.Output> deleteUser(CDeleteUser.Input input) {
        return this.userService.deleteUser(input)
                .onItem().transform(out -> out);
    }

    /**
     * @param input 
     * @return
     */
    @Override
    public Uni<CEditUser.Output> editUser(CEditUser.Input input) {
        return this.userService.updateUser(input).onItem().transform(out -> out);
    }

    /**
     * @param input 
     * @return
     */
    @Override
    public Uni<CGetAllUsers.Output> getAllUsers(CGetAllUsers.Input input) {
        return this.userService.getAllUsers(input).onItem().transform(out-> out);
    }

    /**
     * @param input 
     * @return
     */
    @Override
    public Uni<CFilterUsers.Output> filterUsers(CFilterUsers.Input input) {
        return this.userService.filterUsers(input).onItem().transform(out-> out);
    }

    /**
     * @param input 
     * @return
     */
    @Override
    public Uni<CGetUserByEmail.Output> getUserByEmail(CGetUserByEmail.Input input) {
        return this.userService.getUserByEmail(input).onItem().transform(out -> out);
    }
}

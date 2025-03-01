package org.acme.dto;

import java.util.HashMap;
import java.util.Map;

import org.acme.dto.generic.CMessageAPI;
import org.acme.dto.generic.IGenericDTO;
import org.acme.dto.http.*;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CDTOGestionUser implements IGenericDTO {

    private final Map<String, CMessageAPI> messageMap = new HashMap<>();

    public CDTOGestionUser() {
        messageMap.put(CCreateUser.MSG_NAME, new CMessageAPI(CCreateUser.MSG_NAME, CCreateUser.Input.class));
        messageMap.put(CLoginUser.MSG_NAME, new CMessageAPI(CLoginUser.MSG_NAME, CLoginUser.Input.class));
        messageMap.put(CForgotPassword.MSG_NAME, new CMessageAPI(CForgotPassword.MSG_NAME, CForgotPassword.Input.class));
        messageMap.put(CDeleteUser.MSG_NAME, new CMessageAPI(CDeleteUser.MSG_NAME, CDeleteUser.Input.class));
        messageMap.put(CEditUser.MSG_NAME, new CMessageAPI(CEditUser.MSG_NAME, CEditUser.Input.class));
        messageMap.put(CGetAllUsers.MSG_NAME, new CMessageAPI(CGetAllUsers.MSG_NAME, CGetAllUsers.Input.class));
        messageMap.put(CFilterUsers.MSG_NAME, new CMessageAPI(CFilterUsers.MSG_NAME, CFilterUsers.Input.class));
        messageMap.put(CGetUserByEmail.MSG_NAME, new CMessageAPI(CGetUserByEmail.MSG_NAME, CGetUserByEmail.Input.class));
    }

    @Override
    public String getInterfaceName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public CMessageAPI getMessageAPI(String msgName) {
        return messageMap.get(msgName);
    }

    public interface IHandlerDTOGestionUser{
        Uni<CCreateUser.Output> createUser(CCreateUser.Input input);
        Uni<CLoginUser.Output> loginUser(CLoginUser.Input input);
        Uni<CForgotPassword.Output> forgotPassword(CForgotPassword.Input input);
        Uni<CDeleteUser.Output> deleteUser(CDeleteUser.Input input);
        Uni<CEditUser.Output> editUser(CEditUser.Input input);
        Uni<CGetAllUsers.Output> getAllUsers(CGetAllUsers.Input input);
        Uni<CFilterUsers.Output> filterUsers(CFilterUsers.Input input);
        Uni<CGetUserByEmail.Output> getUserByEmail(CGetUserByEmail.Input input);
    }
}

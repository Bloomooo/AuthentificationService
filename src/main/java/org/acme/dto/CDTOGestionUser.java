package org.acme.dto;

import java.util.HashMap;
import java.util.Map;

import org.acme.dto.generic.CMessageAPI;
import org.acme.dto.generic.IGenericDTO;
import org.acme.dto.http.CCreateUser;
import org.acme.dto.http.CForgotPassword;
import org.acme.dto.http.CLoginUser;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CDTOGestionUser implements IGenericDTO {

    private final Map<String, CMessageAPI> messageMap = new HashMap<>();

    public CDTOGestionUser() {
        messageMap.put(CCreateUser.MSG_NAME, new CMessageAPI(CCreateUser.MSG_NAME, CCreateUser.Input.class));
        messageMap.put(CLoginUser.MSG_NAME, new CMessageAPI(CLoginUser.MSG_NAME, CLoginUser.Input.class));
        messageMap.put(CForgotPassword.MSG_NAME, new CMessageAPI(CForgotPassword.MSG_NAME, CForgotPassword.Input.class));
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
    }
}

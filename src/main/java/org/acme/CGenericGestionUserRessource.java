package org.acme;

import java.lang.reflect.Method;

import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.ws.rs.GET;
import org.acme.dto.CDTOGestionUser;
import org.acme.dto.generic.IMessageAPI;
import org.acme.dto.http.CLoginUser;
import org.acme.handler.CGenericGestionUserHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("")
public class CGenericGestionUserRessource {

    private final ObjectMapper objectMapper;
    private final CDTOGestionUser dtoGestionUser;
    private final CGenericGestionUserHandler genericGestionUserHandler;
    private final Logger logger;

    public CGenericGestionUserRessource(CDTOGestionUser dtoGestionUser,
                                        CGenericGestionUserHandler genericGestionUserHandler) {
        this.objectMapper = new ObjectMapper();
        this.dtoGestionUser = dtoGestionUser;
        this.genericGestionUserHandler = genericGestionUserHandler;
        this.logger = LoggerFactory.getLogger(CGenericGestionUserRessource.class);
    }

    @POST
    public Uni<Object> handleMessage(Object body) {
        try {
            JsonNode jsonNode = this.objectMapper.valueToTree(body);
            String typeMsg = jsonNode.get("typeMsg").asText();

            IMessageAPI messageAPI = this.dtoGestionUser.getMessageAPI(typeMsg);

            Object input = this.objectMapper.treeToValue(jsonNode.get("input"), messageAPI.getTypeInput());

            Method method = this.genericGestionUserHandler.getClass().getMethod(typeMsg, messageAPI.getTypeInput());

            Uni<?> outputUni = (Uni<?>) method.invoke(this.genericGestionUserHandler, input);

            return outputUni.onItem().transform(output -> {
                messageAPI.setOutput(output);
                return messageAPI.getOutput();
            });

        } catch (NoSuchMethodException e) {
            this.logger.error("Méthode inconnue : " + e.getMessage());
            return Uni.createFrom().item("Méthode inconnue : " + e.getMessage());
        } catch (Exception e) {
            this.logger.error("Erreur interne : " + e.getMessage());
            return Uni.createFrom().item("Erreur interne : " + e.getMessage());
        }
    }
}

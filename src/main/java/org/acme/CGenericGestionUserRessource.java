package org.acme;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import org.acme.dto.CDTOGestionUser;
import org.acme.dto.generic.IMessageAPI;
import org.acme.dto.http.CCreateUser;
import org.acme.dto.http.CLoginUser;
import org.acme.handler.CGenericGestionUserHandler;
import org.acme.security.JwtService;
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
    private final JwtService jwtService;
    private final Logger logger;

    public CGenericGestionUserRessource(CDTOGestionUser dtoGestionUser,
                                        CGenericGestionUserHandler genericGestionUserHandler, JwtService jwtService) {
        this.objectMapper = new ObjectMapper();
        this.dtoGestionUser = dtoGestionUser;
        this.genericGestionUserHandler = genericGestionUserHandler;
        this.jwtService = jwtService;
        this.logger = LoggerFactory.getLogger(CGenericGestionUserRessource.class);
    }

    @POST
    public Uni<Object> handleMessage(Object body) {
        try {
            JsonNode jsonNode = this.objectMapper.valueToTree(body);
            String typeMsg = jsonNode.get("typeMsg").asText();
            IMessageAPI messageAPI = this.dtoGestionUser.getMessageAPI(typeMsg);

            if (!messageAPI.getMsgName().equals(CLoginUser.MSG_NAME) && !messageAPI.getMsgName().equals(CCreateUser.MSG_NAME)) {
                String token = jsonNode.get("token").asText();
                if (token == null || token.equals("")) {
                    return Uni.createFrom().item("Token manquant ou format incorrect");
                }

                return this.jwtService.isTokenValid(token)
                        .onItem().transformToUni(success -> {
                            if (!success) {
                                return Uni.createFrom().item("Token invalide");
                            }
                            return processMessage(typeMsg, jsonNode, messageAPI);
                        })
                        .onFailure().recoverWithItem(e -> {
                            this.logger.error("Erreur de validation du token : " + e.getMessage());
                            return "Token invalide";
                        });
            }

            return processMessage(typeMsg, jsonNode, messageAPI);

        } catch (Exception e) {
            this.logger.error("Erreur interne : " + e.getMessage(), e);
            return Uni.createFrom().item("Erreur interne : " + e.getMessage());
        }
    }

    private Uni<Object> processMessage(String typeMsg, JsonNode jsonNode, IMessageAPI messageAPI) {
        try {
            Object input = this.objectMapper.treeToValue(jsonNode.get("input"), messageAPI.getTypeInput());
            Method method = this.genericGestionUserHandler.getClass().getMethod(typeMsg, messageAPI.getTypeInput());
            Uni<?> outputUni = (Uni<?>) method.invoke(this.genericGestionUserHandler, input);

            return outputUni.onItem().transform(output -> {
                messageAPI.setOutput(output);
                return messageAPI.getOutput();
            });
        } catch (JsonProcessingException e) {
            this.logger.error("Erreur de conversion JSON : " + e.getMessage(), e);
            return Uni.createFrom().item("Erreur de conversion JSON");
        } catch (NoSuchMethodException e) {
            this.logger.error("Méthode inconnue : " + e.getMessage(), e);
            return Uni.createFrom().item("Méthode inconnue : " + e.getMessage());
        } catch (IllegalAccessException | InvocationTargetException e) {
            this.logger.error("Erreur d'invocation de méthode : " + e.getMessage(), e);
            return Uni.createFrom().item("Erreur d'invocation de méthode");
        }
    }


}

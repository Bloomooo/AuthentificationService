package org.acme.security;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.config.Environment;
import org.acme.repository.IUserRepository;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;

import java.util.Objects;

@ApplicationScoped
public class JwtService {

    private final Environment environment;
    private final IUserRepository userRepository;

    public JwtService(Environment environment, IUserRepository userRepository) {
        this.environment = environment;
        this.userRepository = userRepository;
    }

    public String generateToken(String username) {
        return Jwt.upn(username).signWithSecret(this.environment.getSecret());
    }

    public String extractUsername(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setVerificationKey(new HmacKey(this.environment.getSecret().getBytes()))
                    .setSkipSignatureVerification()
                    .setRelaxVerificationKeyValidation()
                    .build();

            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);

            String upn = jwtClaims.getClaimValue("upn", String.class);
            System.out.println("UPN: " + upn);
            return upn;
        } catch (Exception e) {
            return "";
        }
    }

    public Uni<Boolean> isTokenValid(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setVerificationKey(new HmacKey(this.environment.getSecret().getBytes()))
                    .setSkipSignatureVerification()
                    .setRelaxVerificationKeyValidation()
                    .build();

            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);

            return this.isTokenContainsUser(token)
                    .onItem().transform(success ->{
                        if(!success){
                            return false;
                        }
                        try {
                            return jwtClaims.getExpirationTime().getValueInMillis() > System.currentTimeMillis();
                        } catch (MalformedClaimException e) {
                            return false;
                        }
                    });
        } catch (Exception e) {
            return Uni.createFrom().item(false);
        }
    }

    @WithSession
    public Uni<Boolean> isTokenContainsUser(String token){
        return this.userRepository.findByUsername(this.extractUsername(token))
                .onItem().transformToUni(user -> {
                    if(user == null){
                        return this.userRepository.findByMail(this.extractUsername(token))
                                .onItem().transform(Objects::nonNull)
                                .onFailure().recoverWithItem(Objects::isNull);
                    }
                    return Uni.createFrom().item(true);
                });
    }
}
package org.acme.security;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.config.Environment;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;

@ApplicationScoped
public class JwtService {

    private final Environment environment;
    private final JWTParser jwtParser;

    public JwtService(Environment environment, JWTParser jwtParser) {
        this.environment = environment;
        this.jwtParser = jwtParser;
    }

    public String generateToken(String username) {
        return Jwt.upn(username).signWithSecret(this.environment.getSecret());
    }

    public String extractUsername(String token) {
        try {
            // Supprimer le préfixe "Bearer " si présent
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Utiliser Jose4j directement pour faire le parsing
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setVerificationKey(new HmacKey(this.environment.getSecret().getBytes()))
                    .setSkipSignatureVerification()
                    .setRelaxVerificationKeyValidation()
                    .build();

            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);

            // Extraire l'UPN claim
            String upn = jwtClaims.getClaimValue("upn", String.class);
            System.out.println("UPN: " + upn);
            return upn;
        } catch (Exception e) {
            return "";
        }
    }
}
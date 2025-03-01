package org.acme.service;

import io.quarkus.qute.Template;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.acme.security.JwtService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.Properties;

@ApplicationScoped
public class CMailSenderService {
    private static final Logger LOGGER = Logger.getLogger(CMailSenderService.class);
    private final Template emailTemplate;
    private final JwtService jwtService;
    private final Vertx vertx;

    @ConfigProperty(name = "MAIL_HOST")
    String mailHost;

    @ConfigProperty(name = "MAIL_PORT")
    String mailPort;

    @ConfigProperty(name = "MAIL_USERNAME")
    String mailUsername;

    @ConfigProperty(name = "MAIL_PASSWORD")
    String mailPassword;

    public CMailSenderService(Template emailTemplate, JwtService jwtService, Vertx vertx) {
        this.emailTemplate = emailTemplate;
        this.jwtService = jwtService;
        this.vertx = vertx;
    }

    public Uni<Boolean> sendEmail(String email) {
        try {
            String link = "http://localhost:4000/resetPassword?token=" + jwtService.generateToken(email);

            String emailContent = emailTemplate
                    .data("link", link)
                    .data("imageCid", "animedle_logo") // Référence CID pour l'image
                    .render();

            return Uni.createFrom().emitter(emitter -> {
                vertx.executeBlocking(promise -> {
                    try {
                        Properties props = new Properties();
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.starttls.enable", "true");
                        props.put("mail.smtp.host", mailHost);
                        props.put("mail.smtp.port", mailPort);

                        Session session = Session.getInstance(props, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(mailUsername, mailPassword);
                            }
                        });

                        MimeMultipart multipart = new MimeMultipart("related");

                        MimeBodyPart htmlPart = new MimeBodyPart();
                        htmlPart.setContent(emailContent, "text/html; charset=utf-8");
                        multipart.addBodyPart(htmlPart);

                        MimeBodyPart imagePart = new MimeBodyPart();
                        DataSource source = new FileDataSource("src/main/resources/images/iconAnimeDle.png");
                        imagePart.setDataHandler(new DataHandler(source));
                        imagePart.setHeader("Content-ID", "<animedle_logo>");
                        imagePart.setDisposition(MimeBodyPart.INLINE);
                        multipart.addBodyPart(imagePart);

                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(mailUsername));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                        message.setSubject("Réinitialisation du mot de passe");
                        message.setContent(multipart);

                        Transport.send(message);
                        promise.complete(true);
                    } catch (Exception e) {
                        LOGGER.error("Échec de l'envoi de l'email via Jakarta Mail", e);
                        promise.fail(e);
                    }
                }, false, result -> {
                    if (result.succeeded()) {
                        LOGGER.info("Email envoyé avec succès via Jakarta Mail");
                        emitter.complete(true);
                    } else {
                        emitter.fail(result.cause());
                    }
                });
            });
        } catch (Exception e) {
            LOGGER.error("Exception attrapée lors de la préparation du mail", e);
            return Uni.createFrom().failure(e);
        }
    }



}


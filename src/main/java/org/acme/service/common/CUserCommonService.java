package org.acme.service.common;

import org.acme.model.User;
import org.acme.repository.IUserRepository;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CUserCommonService {
    private final static String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private final static String MAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";


    public boolean isUsernameValid(String username){
        return username != null && !username.isEmpty();
    }

    public boolean isMailValid(String mail){
        return mail != null && !mail.isEmpty() && mail.matches(MAIL_REGEX);
    }

    public boolean isPasswordValid(String password){
        return password != null && !password.isEmpty() && password.length() >= 8;
    }

    public boolean isUserValid(User user){
        return isUsernameValid(user.getUsername()) && isMailValid(user.getMail()) && isPasswordValid(user.getPassword());
    }

    public boolean isPasswordMatch(String password){
        return password.matches(PASSWORD_REGEX);
    }

    @WithSession
    public Uni<Boolean> isUsernameAlreadyExists(String username, IUserRepository userRepository){
        return userRepository.isUsernameAlreadyExists(username);
    }

    @WithSession
    public Uni<Boolean> isMailAlreadyExists(String mail, IUserRepository userRepository){
        return userRepository.isMailAlreadyExists(mail);
    }
}

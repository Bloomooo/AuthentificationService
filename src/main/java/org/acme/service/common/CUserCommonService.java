package org.acme.service.common;

import org.acme.model.User;
import org.acme.repository.IUserRepository;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CUserCommonService {

    private final IUserRepository userRepository;

    private final static String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private final static String MAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public CUserCommonService(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

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
    public Uni<Boolean> isUsernameAlreadyExists(String username){
        return this.userRepository.isUsernameAlreadyExists(username);
    }

    @WithSession
    public Uni<Boolean> isMailAlreadyExists(String mail){
        return this.userRepository.isMailAlreadyExists(mail);
    }

    @WithSession
    public Uni<List<User>> getAllUsers(){
        return this.userRepository.listAll();
    }

    @WithSession
    public Uni<List<User>> filterUsers(String name){
        return this.userRepository.listAll().onItem().transform(users -> {;
            return users.stream().filter(user -> user.getUsername().toLowerCase().contains(name.toLowerCase())).toList();
        });
    }
}

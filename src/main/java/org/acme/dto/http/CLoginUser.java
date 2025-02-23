package org.acme.dto.http;

import org.acme.model.User;

public class CLoginUser {
    public static final String MSG_NAME = "loginUser";
    public static class Input{
        public String username;
        public String password;
    }

    public static class Output{
        public User user;
        public String message;
        public boolean success;
    }
}

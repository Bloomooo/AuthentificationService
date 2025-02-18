package org.acme.dto.http;

import org.acme.model.User;

public class CCreateUser {

    public final static String MSG_NAME = "createUser";

    public static class Input{
        public String name;
        public String email;
        public String password;
    }

    public static class Output{
        public User user;
        public String message;
        public boolean success;
    }
}

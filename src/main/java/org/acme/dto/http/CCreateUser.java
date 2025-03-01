package org.acme.dto.http;

import org.acme.dto.model.UserDTO;

public class CCreateUser {

    public final static String MSG_NAME = "createUser";

    public static class Input{
        public String username;
        public String password;
        public String mail;
    }

    public static class Output{
        public UserDTO user;
        public String message;
        public boolean success;
    }
}

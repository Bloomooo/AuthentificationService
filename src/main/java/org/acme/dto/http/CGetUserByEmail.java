package org.acme.dto.http;

import org.acme.model.User;

public class CGetUserByEmail {
    public static final String MSG_NAME = "getUserByEmail";

    public static class Input{
        public String email;
    }

    public static class Output{
        public User user;
        public boolean isSuccess;
        public String message;
    }
}

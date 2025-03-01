package org.acme.dto.http;

import org.acme.dto.model.UserDTO;

public class CGetUserByEmail {
    public static final String MSG_NAME = "getUserByEmail";

    public static class Input{
        public String email;
    }

    public static class Output{
        public UserDTO user;
        public boolean isSuccess;
        public String message;
    }
}

package org.acme.dto.http;

import org.acme.dto.model.UserDTO;

public class CSearchUserByToken {
    public static final String MSG_NAME = "searchUserByToken";

    public static class Input {
        public String token;
    }

    public static class Output {
        public UserDTO user;
        public boolean isSuccess;
        public String message;
    }
}

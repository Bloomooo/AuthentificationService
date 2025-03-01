package org.acme.dto.http;

import org.acme.model.User;

public class CSearchUserByToken {
    public static final String MSG_NAME = "searchUserByToken";

    public static class Input {
        public String token;
    }

    public static class Output {
        public User user;
        public boolean isSuccess;
        public String message;
    }
}

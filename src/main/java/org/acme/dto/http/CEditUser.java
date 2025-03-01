package org.acme.dto.http;

import org.acme.model.User;

public class CEditUser {
    public static final String MSG_NAME = "editUser";

    public static class Input{
        public User user;
    }

    public static class Output {
        public boolean isSuccess;
        public String message;
    }
}

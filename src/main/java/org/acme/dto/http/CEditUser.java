package org.acme.dto.http;

import org.acme.dto.model.UserDTO;

public class CEditUser {
    public static final String MSG_NAME = "editUser";

    public static class Input{
        public UserDTO user;
    }

    public static class Output {
        public boolean isSuccess;
        public String message;
    }
}

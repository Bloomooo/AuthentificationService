package org.acme.dto.http;

public class CDeleteUser {
    public static final String MSG_NAME = "deleteUser";

    public static class Input{
        public Long id;
    }

    public static class Output{
        public boolean isSuccess;
        public String message;
    }
}

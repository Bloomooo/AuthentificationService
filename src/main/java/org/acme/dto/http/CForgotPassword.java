package org.acme.dto.http;

public class CForgotPassword {
    public static final String MSG_NAME = "forgotPassword";

    public static class Input{
        public String email;
        public String password;
        public String token;
    }

    public static class Output{
        public Boolean isSuccess;
        public String message;
    }
}

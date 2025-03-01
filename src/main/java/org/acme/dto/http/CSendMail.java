package org.acme.dto.http;

public class CSendMail {
    public static final String MSG_NAME = "sendMail";

    public static class Input{
        public String email;
    }

    public static class Output{
        public boolean isSuccess;
        public String message;
    }
}

package org.acme.dto.http;

import org.acme.model.User;

import java.util.List;

public class CFilterUsers {
    public static final String MSG_NAME = "filterUsers";

    public static class Input{
        public String name;
    }

    public static class Output{
        public List<User> users;
        public boolean isSuccess;
        public String message;
    }
}

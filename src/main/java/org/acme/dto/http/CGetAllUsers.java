package org.acme.dto.http;

import org.acme.model.User;

import java.util.ArrayList;
import java.util.List;

public class CGetAllUsers {
    public static final String MSG_NAME = "getAllUsers";

    public static class Input{

    }

    public static class Output{
        public List<User> users;
        public boolean isSuccess;
        public String message;
    }
}

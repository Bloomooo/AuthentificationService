package org.acme.dto.http;

import org.acme.dto.model.UserDTO;
import java.util.List;

public class CGetAllUsers {
    public static final String MSG_NAME = "getAllUsers";

    public static class Input{

    }

    public static class Output{
        public List<UserDTO> users;
        public boolean isSuccess;
        public String message;
    }
}

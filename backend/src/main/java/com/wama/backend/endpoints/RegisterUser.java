package com.wama.backend.endpoints;

import com.wama.backend.DatabaseManager;
import com.wama.backend.HttpStatus;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends com.wama.LogClass implements Endpoint {
    public boolean validateParameters(Map<String, String> parameters) {
        if (parameters == null) {
            error("Parameters are null");
            return false;
        }
        if (!parameters.containsKey("username") && !parameters.containsKey("email") && !parameters.containsKey("password")) {
            error("Parameters are missing");
            return false;
        }
        String username = parameters.get("username");
        String email = parameters.get("email");
        String password = parameters.get("password");

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            debug("Parameters are empty");
            debug("Username: " + username);
            debug("Email: " + email);
            debug("Password: " + password);
            return false;
        }
        return true;
    }

    public HttpStatus handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        info("Registering user");
        if (validateParameters(parameters)) {
            String username = parameters.get("username");
            String email = parameters.get("email");
            String password = parameters.get("password");
            int userID;
            debug("Registering user with username: " + username + " and email: " + email);
            try {
                DatabaseManager.insertRecord("Users", new String[]{"username", "email"}, new String[]{username, email});
                ArrayList<HashMap<String, String>> user = DatabaseManager.selectRecords("Users", new String[]{"id"}, "username='" + username + "'");
                assert user != null;
                userID = Integer.parseInt(user.get(0).get("id"));
            } catch (Exception e) {
                if (e.getMessage().contains("UNIQUE constraint failed"))
                    return HttpStatus.CONFLICT;
                error("Error registering user: " + e.getMessage(), e);
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
            try {
                DatabaseManager.insertRecord("UserPasswords",
                        new String[]{"user_id", "password"},
                        new String[]{Integer.toString(userID), password});
            } catch (Exception e) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
            info("User " + username + "registered successfully");
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
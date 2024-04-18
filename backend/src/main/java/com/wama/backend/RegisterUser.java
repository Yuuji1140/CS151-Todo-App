package com.wama.backend;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterUser implements Endpoint {
    public boolean validateParameters(Map<String, String> parameters) {
        if (parameters == null) {
            System.out.println("Parameters are null");
            return false;
        }
        if (!parameters.containsKey("username") && !parameters.containsKey("email") && !parameters.containsKey("password")) {
            System.out.println("Parameters are missing");
            return false;
        }
        String username = parameters.get("username");
        String email = parameters.get("email");
        String password = parameters.get("password");

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Parameters are empty");
            System.out.println("Username: " + parameters.get("username"));
            System.out.println("Email: " + parameters.get("email"));
            System.out.println("Password: " + parameters.get("password"));
            return false;
        }
        return true;
    }

    public HttpStatus handleGetRequest(Map<String, String> parameters, OutputStream outputStream) throws IOException {
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus handlePostRequest(Map<String, String> parameters, OutputStream outputStream) throws IOException {
        System.out.println("Registering user");
        if (validateParameters(parameters)) {
            String username = parameters.get("username");
            String email = parameters.get("email");
            String password = parameters.get("password");
            int userID;
            System.out.println("Registering user with username: " + username + " and email: " + email);
            boolean success;
            try {
                DatabaseManager.insertRecord("Users", new String[]{"username", "email"}, new String[]{username, email});
                ArrayList user = DatabaseManager.selectRecords("Users", new String[]{"id"}, "username='" + username + "'");
                userID = Integer.parseInt(((HashMap) user.get(0)).get("id").toString());
                success = true;
            } catch (Exception e) {
                if (e.getMessage().contains("UNIQUE constraint failed"))
                    return HttpStatus.CONFLICT;
                System.out.println("Error registering user: " + e.getMessage());
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
            if (!success) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
            try {
                DatabaseManager.insertRecord("UserPasswords",
                        new String[]{"user_id", "password"},
                        new String[]{Integer.toString(userID), password});
            } catch (Exception e) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
            System.out.println("User registered successfully");
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
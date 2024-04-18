package com.wama.backend;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

public class AuthUser implements Endpoint {

    public boolean validateParameters(Map<String, String> parameters) {
        if (parameters == null)
            return false;
        if (parameters.containsKey("username") && parameters.containsKey("password"))
            return !parameters.get("username").isEmpty() && !parameters.get("password").isEmpty();
        return false;
    }

    public HttpStatus handleGetRequest(Map<String, String> parameters, OutputStream outputStream) throws IOException {
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus handlePostRequest(Map<String, String> parameters, OutputStream outputStream) throws IOException {
        String username = parameters.get("username");
        String password = parameters.get("password");

        if (username != null && password != null) {
            System.out.println("Authenticating user with username: " + username);
            // Here, you can check the provided credentials against a database or other storage
            // For now, let's assume the credentials are valid
            String userId = UUID.randomUUID().toString();

            System.out.println("User authenticated successfully with ID: " + userId);

            String response = "POST request for AuthUser endpoint\n"
                    + "Username: " + username + "\n"
                    + "Password: " + password;

            return HttpStatus.OK;
        } else {
            System.out.println("Missing required parameters" + parameters.toString());
            return HttpStatus.BAD_REQUEST;
        }
    }
}
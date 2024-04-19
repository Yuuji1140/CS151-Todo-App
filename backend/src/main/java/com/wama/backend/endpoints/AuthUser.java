package com.wama.backend.endpoints;

import com.wama.backend.HttpStatus;

import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

public class AuthUser extends com.wama.LogClass implements Endpoint {

    public boolean validateParameters(Map<String, String> parameters) {
        if (parameters == null)
            return false;
        if (parameters.containsKey("username") && parameters.containsKey("password"))
            return !parameters.get("username").isEmpty() && !parameters.get("password").isEmpty();
        return false;
    }

    public HttpStatus handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        // TODO: Actual authentication logic
        String username = parameters.get("username");
        String password = parameters.get("password");
        if (username != null && password != null) {
            info("Authenticating user with username: " + username);
                        // Here, you can check the provided credentials against a database or other storage
            // For now, let's assume the credentials are valid
            String userId = UUID.randomUUID().toString();

            debug("User authenticated successfully. User ID: " + userId);

            return HttpStatus.OK;
        } else {
            error("Invalid parameters provided for authentication.");
            return HttpStatus.BAD_REQUEST;
        }
    }
}
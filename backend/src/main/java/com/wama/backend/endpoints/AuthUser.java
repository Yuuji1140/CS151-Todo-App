package com.wama.backend.endpoints;

import com.wama.User;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class AuthUser extends com.wama.LogClass implements Endpoint {

    public boolean validParameters(Map<String, String> parameters) {
        if (parameters == null)
            return false;
        if (parameters.containsKey("username")
                && parameters.containsKey("password")
                && parameters.containsKey("type"))
            return !parameters.get("username").isEmpty()
                    && !parameters.get("password").isEmpty()
                    && !parameters.get("type").isEmpty();
        return false;
    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        // TODO: Actual authentication logic
        String username = parameters.get("username");
        String password = parameters.get("password");
        String type = parameters.get("type");
        if (username != null && password != null) {
            HashMap<String, String> arguments = new HashMap<>();
            info("Authenticating user with username: " + username);
            User.UserType userType = type.equals("Customer") ? User.UserType.CUSTOMER : User.UserType.EMPLOYEE;
            User user;
            // Lookup email from username
            if (userType == User.UserType.CUSTOMER) {
                user = new com.wama.Customer(username, password, "", "");
            } else {
                user = new com.wama.Employee(username, password, "", "");
            }
            User loggedInUser = user.loginUser(password);
            if (loggedInUser != null) {
                arguments.put("authToken", user.getAuthToken());
                arguments.put("id", user.getId());
                arguments.put("username", user.getUsername());
                arguments.put("email", user.getEmail());
                arguments.put("type", type);
                return new HttpResponse(HttpStatus.OK, arguments);
            } else {
                error("Failed to authenticate user with username: " + username);
                return new HttpResponse(HttpStatus.UNAUTHORIZED, arguments);
            }
        } else {
            error("Invalid parameters provided for authentication.");
            return new HttpResponse(HttpStatus.BAD_REQUEST, new HashMap<>());
        }
    }
}
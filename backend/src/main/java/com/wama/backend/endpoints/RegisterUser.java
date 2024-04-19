package com.wama.backend.endpoints;

import com.wama.User;
import com.wama.backend.HttpStatus;


import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

public class RegisterUser extends com.wama.LogClass implements Endpoint {
    public boolean validateParameters(Map<String, String> parameters) {
        if (parameters == null) {
            error("Parameters are null");
            return false;
        }
        if (!parameters.containsKey("username") &&
                !parameters.containsKey("email") &&
                !parameters.containsKey("password") &&
                !parameters.containsKey("type")) { // Customer or Employee
            error("Parameters are missing");
            return false;
        }
        String username = parameters.get("username");
        String email = parameters.get("email");
        String password = parameters.get("password");
        String type = parameters.get("type");

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || type.isEmpty()) {
            debug("Parameters are empty");
            debug("Username: " + username);
            debug("Email: " + email);
            debug("Password: " + password);
            return false;
        }
        return true;
    }

//    public HttpStatus handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
//        return HttpStatus.BAD_REQUEST;
//    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        info("Registering user");
        if (validateParameters(parameters)) {
            HashMap<String, String> arguments = new HashMap<>();
            String username = parameters.get("username");
            String email = parameters.get("email");
            String password = parameters.get("password");
            String type = parameters.get("type");
            User.UserType userType = type.equals("Customer") ? User.UserType.CUSTOMER : User.UserType.EMPLOYEE;
            User user;
            if (userType == User.UserType.CUSTOMER) {
                user = new com.wama.Customer(username, password, email, "");
            } else {
                user = new com.wama.Employee(username, password, email, "");
            }
            try {
                if (user.registerNewUser(password) == null) {
                    // Theoretically impossible to get here. If the user is null, an exception would have been thrown.
                    arguments.put("error", "User already exists");
                    return new HttpResponse(HttpStatus.BAD_REQUEST, arguments);
                }
            } catch (IllegalArgumentException e) {
                error("Error registering user: " + e.getMessage(), e);
                arguments.put("error", e.getMessage());
                return new HttpResponse(HttpStatus.BAD_REQUEST, arguments);
            }
            arguments.put("username", username);
            arguments.put("email", email);
            arguments.put("type", type);
            arguments.put("authToken", user.getAuthToken());
            return new HttpResponse(HttpStatus.OK, arguments);
        } else {
            return new HttpResponse(HttpStatus.BAD_REQUEST, new HashMap<>());
        }
    }
}
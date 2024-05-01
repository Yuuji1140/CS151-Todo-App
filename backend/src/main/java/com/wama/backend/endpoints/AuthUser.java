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
        String username = parameters.get("username");
        String password = parameters.get("password");
        String type = parameters.get("type");

        info("Authenticating user with username: " + username);
        User.UserType userType = type.equals("Customer") ? User.UserType.CUSTOMER : User.UserType.EMPLOYEE;
        User user;

        if (userType == User.UserType.CUSTOMER) {
            user = new com.wama.Customer(username);
        } else {
            user = new com.wama.Employee(username);
        }
        User loggedInUser = user.loginUser(password);
        if (loggedInUser != null) {
            return new HttpResponse(HttpStatus.OK, loggedInUser.getParameters());
        } else {
            error("Failed to authenticate user with username: " + username);
            return new HttpResponse(HttpStatus.UNAUTHORIZED, new HashMap<>());
        }
    }
}
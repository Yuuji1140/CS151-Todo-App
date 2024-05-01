package com.wama.backend.endpoints;

import com.wama.Customer;
import com.wama.Employee;
import com.wama.User;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class AuthUser extends com.wama.LogClass implements Endpoint {

    public boolean validParameters(Map<String, String> parameters) {
        if (parameters == null)
            return false;
        if (parameters.containsKey("username")
                && parameters.containsKey("password"))
            return !parameters.get("username").isEmpty()
                    && !parameters.get("password").isEmpty();
        return false;
    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        String username = parameters.get("username");
        String password = parameters.get("password");


        info("Authenticating user with username: " + username);
        User assumeEmployee = new Employee(username);
        User user = User.getCurrentType(username) == User.UserType.CUSTOMER ? new Customer(username) : assumeEmployee;
        User loggedInUser = user.loginUser(password);
        if (loggedInUser != null) {
            debug("here" + loggedInUser.getParameters().toString());
            return new HttpResponse(HttpStatus.OK, loggedInUser.getParameters());
        } else {
            error("Failed to authenticate user with username: " + username);
            return new HttpResponse(HttpStatus.UNAUTHORIZED, new HashMap<>());
        }
    }
}
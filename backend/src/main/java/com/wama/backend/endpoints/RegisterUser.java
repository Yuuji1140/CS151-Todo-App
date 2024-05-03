package com.wama.backend.endpoints;

import com.wama.User;
import com.wama.DatabaseManager;


import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class RegisterUser extends com.wama.LogClass implements Endpoint {
    public boolean validParameters(Map<String, String> parameters) {
        if (parameters == null) {
            error("Parameters are null");
            return false;
        }
        if (!parameters.containsKey("username") &&
                !parameters.containsKey("email") &&
                !parameters.containsKey("password") &&
                !parameters.containsKey("type") &&
                !parameters.containsKey("company_name") &&
                !parameters.containsKey("name") &&
                !parameters.containsKey("phone") &&
                !parameters.containsKey("address")) {
            error("Parameters are missing");
            return false;
        }
        String username = parameters.get("username");
        String email = parameters.get("email");
        String password = parameters.get("password");
        String type = parameters.get("type");
        String company_name = parameters.get("company_name");
        String name = parameters.get("name");
        String phone = parameters.get("phone");
        String address = parameters.get("address");

        if (username.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                type.isEmpty() ||
                name.isEmpty() ||
                phone.isEmpty() ||
                address.isEmpty() ||
                (company_name.isEmpty() && type.equals("Customer"))) {
            debug("Parameters are empty");
            debug("Username: " + username);
            debug("Email: " + email);
            debug("Password: " + password);
            debug("Type: " + type);
            debug("Company Name: " + company_name);
            debug("Name: " + name);
            debug("Phone: " + phone);
            debug("Address: " + address);
            return false;
        }
        return true;
    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        info("Registering user");
        HashMap<String, String> arguments = new HashMap<>();
        String username = parameters.get("username");
        String email = parameters.get("email");
        String password = parameters.get("password");
        String type = parameters.get("type");
        String name = parameters.get("name");
        String phone = parameters.get("phone");
        String address = parameters.get("address");

        User.UserType userType = type.equals("Customer") ? User.UserType.CUSTOMER : User.UserType.EMPLOYEE;
        String company_name = userType == User.UserType.CUSTOMER ? parameters.get("company_name") : null;
        User registeredUser;
        try {
            registeredUser = registerUser(username, email, password, userType, company_name, name, phone, address);
            if (registeredUser == null) {
                // Theoretically impossible to get here. If the user is null, an exception would have been thrown.
                arguments.put("error", "User already exists");
                return new HttpResponse(HttpStatus.BAD_REQUEST, arguments);
            }
        } catch (IllegalArgumentException e) {
            error("Error registering user: " + e.getMessage(), e);
            arguments.put("error", e.getMessage());
            return new HttpResponse(HttpStatus.BAD_REQUEST, arguments);
        }
        return new HttpResponse(HttpStatus.OK, registeredUser.getParameters());
    }

    private User registerUser(String username, String email, String password, User.UserType userType,
                              String companyName, String name, String phone, String address) {
        String userId = UUID.randomUUID().toString();


        try {
            DatabaseManager.insertRecord("Users",
                    new String[]{"id", "username", "email", "user_type", "company_name", "name", "phone", "address"},
                    new String[]{userId, username, email, userType.toString(), companyName, name, phone, address});

            DatabaseManager.insertRecord("UserPasswords",
                    new String[]{"user_id", "password"},
                    new String[]{userId, password});

            HashMap<String, String> userValues = new HashMap<>();
            userValues.put("id", userId);
            userValues.put("username", username);
            userValues.put("email", email);
            userValues.put("type", userType.toString());
            userValues.put("company_name", companyName);
            userValues.put("name", name);
            userValues.put("phone", phone);
            userValues.put("address", address);
            return new User(userValues);
        } catch (Exception e) {
            return null;
        }
    }
}
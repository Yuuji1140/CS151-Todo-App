package com.wama;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class User extends LogClass {
    private String id;
    private final String username;
    public UserType type;
    private String email;
    private String company_name;
    private String name;
    private String phone;
    private String address;

    protected User(String username) {
        this.username = username;
        this.id = getCurrentId();
        if (this.id != null && !this.id.isEmpty()) {
            this.type = getCurrentType(this.username);
        }

    }

    protected User(String username, UserType type, String email, String company_name, String name, String phone, String address) {
        this.username = username;
        this.type = type;
        this.email = email;
        this.id = getCurrentId();
        this.company_name = company_name;
        this.name = name;
        this.phone = phone;
        this.address = address;
        if (this.id.isEmpty()) {
            debug("User " + username + " not found in database");
        }
        debug("Creating blank user object for " + username);
    }

    public User registerNewUser(String password) throws IllegalArgumentException {
        debug("Registering user with username: " + username + " and email: " + email);
        if (!this.id.isEmpty()) {
            // Already registered
            return this;
        }
        try {
            this.id = UUID.randomUUID().toString();
            DatabaseManager.insertRecord("Users",
                    new String[]{"id", "username", "email", "user_type", "company_name", "name", "phone", "address"},
                    new String[]{this.id, username, email, type.toString(), company_name, name, phone, address});
        } catch (Exception e) {
            error("Error registering user: " + e.getMessage(), e);
            this.id = null;
            throw new IllegalArgumentException(!e.getMessage().contains("UNIQUE constraint failed")
                    ? e.getMessage() : "Username or email already exists");
        }
        // This is a valid user, and we just placed them in the table. Store their password
        try {
            DatabaseManager.insertRecord("UserPasswords",
                    new String[]{"user_id", "password"},
                    new String[]{this.id, password});
        } catch (Exception e) {
            error("Error registering user: " + e.getMessage(), e);
            throw new IllegalArgumentException("Unable to register user");
        }
        info("User " + username + " registered successfully.");
        return this;
    }

    public User loginUser(String password) {
        if (this.id.isEmpty()) {
            // Already logged in or not registered, don't tell them for security reasons
            return null;
        }
        ArrayList<HashMap<String, String>> user = DatabaseManager.selectRecords("Users",
                new String[]{"id", "username", "name", "email", "phone", "company_name", "address", "user_type"},
                "username='" + username + "'");
        if (user == null || user.size() != 1) {
            return null;
        }
        ArrayList<HashMap<String, String>> userPassword = DatabaseManager.selectRecords("UserPasswords",
                new String[]{"password"},
                "user_id=\"" + this.id + "\"");
        if (userPassword == null || userPassword.size() != 1) {
            return null;
        }
        if (!userPassword.get(0).get("password").equals(password)) {
            return null;
        }
        this.id = user.get(0).get("id");
        this.email = user.get(0).get("email");
        this.company_name = user.get(0).get("company_name");
        this.name = user.get(0).get("name");
        this.phone = user.get(0).get("phone");
        this.address = user.get(0).get("address");
        info("User " + username + " logged in successfully.");
        return this;
    }

    public HashMap<String, String> getParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("username", username);
        parameters.put("email", email);
        parameters.put("type", type.name());
        parameters.put("company_name", company_name);
        parameters.put("name", name);
        parameters.put("phone", phone);
        parameters.put("address", address);
        return parameters;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
    	return type.name();
    }
    
    public String getEmail() {
        return email;
    }

    private String getCurrentId() {
        ArrayList<HashMap<String, String>> user = DatabaseManager.selectRecords("Users", new String[]{"id"}, "username='" + username + "'");
        if (user == null || user.size() != 1) {
            return "";
        }
        return user.get(0).get("id");
    }

    public static UserType getCurrentType(String username) {
    	ArrayList<HashMap<String, String>> user = DatabaseManager.selectRecords("Users", new String[]{"user_type"}, "username='" + username + "'");
    	if (user == null || user.size() != 1) {
    		return null;
    	}
    	return UserType.valueOf(user.get(0).get("user_type"));
    }

    public void logout() {
        /*
        The auth token and ID are all that validate a user.
         */
        this.id = null;}

    public enum UserType {
        CUSTOMER,
        EMPLOYEE
    }

    // Abstract things Users can do like getOrders, getInventory, createOrders, etc. Some will throw a NotImplementedException and others will implement
}
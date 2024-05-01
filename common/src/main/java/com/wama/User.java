package com.wama;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class User extends LogClass {
    private String id;
    private final String username;
    private String authToken;
    public final UserType type;
    private  String email;

    protected User(String username, UserType type, String email, String authToken) {
        this.username = username;
        this.type = type;
        this.email = email;
        this.id = getCurrentId();
        if (validAuthToken()) {
            this.authToken = authToken;
            debug("User " + username + " logged in with token");
        } else {
            if (this.id.isEmpty()) {
                debug("User " + username + " not found in database");
            }
            this.authToken = null;
            debug("Creating blank user object for " + username);
        }
    }

    public User registerNewUser(String password) throws IllegalArgumentException {
        debug("Registering user with username: " + username + " and email: " + email);
        if (this.id == null) {
            // Already registered
            return this;
        }
        try {
            this.id = UUID.randomUUID().toString();
            DatabaseManager.insertRecord("Users",
                    new String[]{"id", "username", "email"},
                    new String[]{this.id, username, email});
        } catch (Exception e) {
            error("Error registering user: " + e.getMessage(), e);
            this.id = null;
            throw new IllegalArgumentException(!e.getMessage().contains("UNIQUE constraint failed")
                    ? e.getMessage() : "Username or email already exists");
        }
        // This is a valid user, and we just placed them in the table. Store their password, generate a token and return the object
        generateAuthToken();
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
        if (this.username == null || getCurrentId().isEmpty()) {
            // Already logged in or not registered, don't tell them for security reasons
            return null;
        }
        ArrayList<HashMap<String, String>> user = DatabaseManager.selectRecords("Users",
                new String[]{"id"}, "username='" + username + "'");
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
        generateAuthToken();
        return this;
    }

    private void generateAuthToken() {
        // Generate a random 128-bit token
        String token = UUID.randomUUID().toString();
        this.authToken = token;
        // SQLite formatted date 24 hours from now
        long expiresAtMillis = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
        try {
            // Delete any auth token that exists
            DatabaseManager.deleteRecord("AuthTokens", "user_id=\"" + id + "\"");
        } catch (Exception e) {
            debug("Error deleting auth token: " + e.getMessage());
        }
        try {
            DatabaseManager.insertRecord("AuthTokens",
                    new String[]{"user_id", "auth_token", "expires_at"},
                    new String[]{id, token, String.valueOf(expiresAtMillis)});
        } catch (Exception e) {
            error("Error generating auth token: " + e.getMessage(), e);
        }
    }

    private boolean validAuthToken() {
        if (authToken == null) {
            return false;
        }
        String condition = id == null || id.isEmpty() ? null : "user_id=" + id;
        ArrayList<HashMap<String, String>> authTokens = DatabaseManager.selectRecords("AuthTokens",
                new String[]{"auth_token"}, condition);
        // TODO: Check if token is expired
        if (authTokens == null || authTokens.size() != 1) {
            return false;
        }
        return authTokens.get(0).get("auth_token").equals(getAuthToken());
    }

    public String getAuthToken() {
        return authToken;
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

    public void logout() {
        /*
        The auth token and ID are all that validate a user.
         */
        this.id = null;
        this.authToken = null;
    }

    public enum UserType {
        CUSTOMER,
        EMPLOYEE
    }

    // Abstract things Users can do like getOrders, getInventory, createOrders, etc. Some will throw a NotImplementedException and others will implement
}
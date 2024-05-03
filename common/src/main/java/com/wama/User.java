package com.wama;

import java.util.ArrayList;
import java.util.HashMap;

public class User extends LogClass {
    private String id;
    private final String username;
    public UserType type;
    private String email;
    private String company_name;
    private String company_id;
    private String name;
    private String phone;
    private String address;

    public User(String username) {
        // To login
        this.username = username;
        this.id = getCurrentId();
        if (!this.id.isEmpty()) {
            this.type = getTypeFromUsername(this.username);
        }

    }

    public User(String username, UserType type, String email, String company_name, String name, String phone, String address) {
        // To register
        this.username = username;
        this.type = type;
        this.email = email;
        this.id = getCurrentId();
        this.company_name = company_name;
        this.company_id = companyNameToId(company_name);
        this.name = name;
        this.phone = phone;
        this.address = address;
        if (this.id.isEmpty()) {
            debug("User " + username + " not found in database");
        }
        debug("Creating blank user object for " + username);
    }

    public User(HashMap<String, String> userValues) {
        // Unpack them from the frontend
        this.id = userValues.get("id");
        this.username = userValues.get("username");
        this.type = UserType.valueOf(userValues.get("type"));
        this.email = userValues.get("email");
        this.company_name = userValues.get("company_name");
        this.company_id = companyNameToId("company_id");
        this.name = userValues.get("name");
        this.phone = userValues.get("phone");
        this.address = userValues.get("address");
    }

    @Override
    public String toString() {
        return getParameters().toString();
    }

    public static String companyNameToId(String companyName) {
        ArrayList<HashMap<String, String>> customer = DatabaseManager.selectRecords("Customers", new String[]{"id"}, "company_name='" + companyName + "'");
        if (customer == null || customer.size() != 1) {
            return null;
        }
        return customer.get(0).get("id");
    }

    public HashMap<String, String> getParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("username", username);
        parameters.put("email", email);
        parameters.put("type", type.name());
        parameters.put("company_name", company_name);
        parameters.put("company_id", company_id);
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

    public String getCompanyName() {
        return company_name;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCompanyId() {
        return company_id;
    }

    private String getCurrentId() {
        ArrayList<HashMap<String, String>> user = DatabaseManager.selectRecords("Users", new String[]{"id"}, "username='" + username + "'");
        if (user == null || user.size() != 1) {
            return "";
        }
        return user.get(0).get("id");
    }

    public static UserType getTypeFromUsername(String username) {
        ArrayList<HashMap<String, String>> user = DatabaseManager.selectRecords("Users", new String[]{"user_type"}, "username='" + username + "'");
        if (user == null || user.size() != 1) {
            return null;
        }
        String type = user.get(0).get("user_type").toUpperCase();
        return UserType.valueOf(type);
    }

    public void logout() {
        /*
        The auth token and ID are all that validate a user.
         */
        this.id = null;
    }

    public enum UserType {
        CUSTOMER,
        EMPLOYEE
    }
}
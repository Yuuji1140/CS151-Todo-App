package com.wama;

import java.util.ArrayList;
import java.util.HashMap;


public class Customer extends User {
    private String name;
    private String email;
    private String address;
    private String phone;

    public Customer(String username, String password, String email, String authToken) {
        super(username, UserType.CUSTOMER, email, authToken);
        if (authToken != null) {
            debug("Employee " + username + " logged in with token");
        } else {
            debug("Creating blank employee object for " + username);
        }
    }

    // Implement login
    //@Override
    public boolean login() {
        String[] columns = {"id", "password"};
        String condition = "username = ?";
        ArrayList<HashMap<String, String>> users = DatabaseManager.selectRecords("Users", columns, condition);

        if (users.size() != 1) {
            return false;
        }
        String hashedPassword = users.get(0).get("password");
//        if (hashedPassword.equals(this.getPassword())) {
//            this.id = Integer.parseInt(users.get(0).get("id"));
//        }

        return false;
    }

    // Implement placeholder
    public void placeOrder() {

    }

    // Implement
    public void addInventory() {

    }
}

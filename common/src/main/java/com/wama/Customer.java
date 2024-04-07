package com.wama;

import java.util.ArrayList;
import java.util.HashMap;

import com.wama.backend.DatabaseManager;

public class Customer extends User {
    private String company;
    private String email;

    public Customer(String username, String password) {
        super(username, password);
    }

    // Implement login
    @Override
    public boolean login() {
        String[] columns = {"id", "password"};
        String condition = "username = ?";
        ArrayList<HashMap<String, String>> users = DatabaseManager.selectRecords("Users", columns, condition);

        if (users.size() != 1) {
            return false;
        }
        String hashedPassword = users.get(0).get("password");
        if (hashedPassword.equals(this.getPassword())) {
            this.id = Integer.parseInt(users.get(0).get("id"));
        }

        return false;
    }
    
    // Implement placeholder
    public void placeOrder() {

    }

    // Implement
    public void addInventory() {
        
    }
}

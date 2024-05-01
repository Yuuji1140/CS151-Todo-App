package com.wama;

import java.util.ArrayList;
import java.util.HashMap;


public class Customer extends User {
    private String company_name;

    public Customer(String username) {
        super(username, UserType.CUSTOMER);
    }

    public Customer(String username, String email, String company_name, String name, String phone, String address) {
        super(username, UserType.CUSTOMER, email, company_name, name, phone, address);
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

package com.wama;

import java.util.ArrayList;
import java.util.HashMap;


public class Customer extends User {
    private String company_name;

    public Customer(String username) {
        super(username);
    }

    public Customer(String username, String email, String company_name, String name, String phone, String address) {
        super(username, UserType.CUSTOMER, email, company_name, name, phone, address);
    }


    // Implement placeholder
    public void placeOrder() {

    }

    // Implement
    public void addInventory() {

    }
}

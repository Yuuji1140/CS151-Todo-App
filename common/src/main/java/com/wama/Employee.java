package com.wama;

public class Employee extends User {

    public Employee(String username, String password, String email, String authToken) {
        super(username, UserType.EMPLOYEE, email, authToken);
        if (authToken != null) {
            debug("Employee " + username + " logged in with token");
        } else {
            debug("Creating blank employee object for " + username);
        }
    }

    public void processShipment() {

    }

    public void inputInventory() {
        
    }
}

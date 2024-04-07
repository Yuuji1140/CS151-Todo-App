package com.wama;

public class Employee extends User {

    public Employee(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean login() {
        return false;
    }
    
    public void processShipment() {

    }

    public void inputInventory() {
        
    }
}

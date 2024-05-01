package com.wama;

public class Employee extends User {

    public Employee(String username) {
        super(username);
    }

    public Employee(String username, String email, String name, String phone, String address) {
        super(username, UserType.EMPLOYEE, email, null, name, phone, address);
    }

    public void processShipment() {

    }

    public void inputInventory() {
        
    }
}

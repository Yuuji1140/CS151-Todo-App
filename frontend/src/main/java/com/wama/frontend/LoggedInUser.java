package com.wama.frontend;

public class LoggedInUser {
    private static LoggedInUser instance;

    private String id;
    private String username;
    private String email;
    private String companyName;
    private String companyId;
    private String name;
    private String phone;
    private String address;

    private LoggedInUser() {}

    public static LoggedInUser getInstance() {
        if (instance == null) {
            instance = new LoggedInUser();
        }
        return instance;
    }

    public void setUser(String id, String username, String email, String companyName,
                        String companyId, String name, String phone, String address) {
        instance.id = id;
        instance.username = username;
        instance.email = email;
        instance.companyName = companyName;
        instance.companyId = companyId;
        instance.name = name;
        instance.phone = phone;
        instance.address = address;
    }

    public String getUserId() {
        return instance.id;
    }

    public String getUsername() {
        return instance.username;
    }

    public String getEmail() {
        return instance.email;
    }

    public String getCompanyName() {
        return instance.companyName;
    }

    public String getName() {
        return instance.name;
    }

    public String getPhone() {
        return instance.phone;
    }

    public String getAddress() {
        return instance.address;
    }

    @Override
    public String toString() {
        return "LoggedInUser{" +
                "id='" + instance.id + '\'' +
                ", username='" + instance.username + '\'' +
                ", email='" + instance.email + '\'' +
                ", companyName='" + instance.companyName + '\'' +
                ", companyId='" + instance.companyId + '\'' +
                ", name='" + instance.name + '\'' +
                ", phone='" + instance.phone + '\'' +
                ", address='" + instance.address + '\'' +
                '}';
    }

    public void logout() {
        instance = null;
    }
}
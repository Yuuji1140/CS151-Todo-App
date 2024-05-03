package com.wama.frontend;

import java.util.HashMap;
import com.wama.User;

public class LoggedInUser {
    private static LoggedInUser instance;
    private User user;

    private LoggedInUser(HashMap<String, String> userValues) {
        this.user = new User(userValues);
    }

    public static LoggedInUser getInstance(HashMap<String, String> userValues) {
        if (instance == null) {
            instance = new LoggedInUser(userValues);
        }
        return instance;
    }

    public String getUserId() {
        return user.getId();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getCompanyName() {
        return user.getCompanyName();
    }

    public String getName() {
        return user.getName();
    }

    public String getPhone() {
        return user.getPhone();
    }

    public String getAddress() {
        return user.getAddress();
    }

    @Override
    public String toString() {
        return user.toString();
    }

    public void logout() {
        instance = null;
        user.logout();
    }
}
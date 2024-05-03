package com.wama.backend.endpoints;

import com.wama.Customer;
import com.wama.DatabaseManager;
import com.wama.Employee;
import com.wama.User;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthUser extends com.wama.LogClass implements Endpoint {

    public boolean validParameters(Map<String, String> parameters) {
        if (parameters == null)
            return false;
        if (parameters.containsKey("username")
                && parameters.containsKey("password"))
            return !parameters.get("username").isEmpty()
                    && !parameters.get("password").isEmpty();
        return false;
    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        String username = parameters.get("username");
        String password = parameters.get("password");

        info("Authenticating user with username: " + username);
        User assumeEmployee = new Employee(username);
        User user = User.getTypeFromUsername(username) == User.UserType.CUSTOMER ? new Customer(username) : assumeEmployee;
        User loggedInUser = loginUser(username, password);
        if (loggedInUser != null) {
            debug("here" + loggedInUser.getParameters().toString());
            return new HttpResponse(HttpStatus.OK, loggedInUser.getParameters());
        } else {
            error("Failed to authenticate user with username: " + username);
            return new HttpResponse(HttpStatus.UNAUTHORIZED, new HashMap<>());
        }
    }

    private User loginUser(String username, String password) {
        ArrayList<HashMap<String, String>> userRecords = DatabaseManager.selectRecords("Users",
                new String[]{"id", "username", "name", "email", "phone", "company_name", "address", "user_type"},
                "username='" + username + "'");

        if (userRecords == null || userRecords.size() != 1)
            return null;

        HashMap<String, String> userRecord = userRecords.get(0);
        String userId = userRecord.get("id");

        ArrayList<HashMap<String, String>> passwordRecords = DatabaseManager.selectRecords("UserPasswords",
                new String[]{"password"}, "user_id=\"" + userId + "\"");

        if (passwordRecords == null || passwordRecords.size() != 1)
            return null;

        String storedPassword = passwordRecords.get(0).get("password");

        if (!storedPassword.equals(password))
            return null;

        User.UserType userType = User.UserType.valueOf(userRecord.get("user_type").toUpperCase());
        HashMap<String, String> userValues = new HashMap<>();

        userValues.put("id", userId);
        userValues.put("username", userRecord.get("username"));
        userValues.put("name", userRecord.get("name"));
        userValues.put("email", userRecord.get("email"));
        userValues.put("phone", userRecord.get("phone"));
        userValues.put("company_name", userRecord.get("company_name"));
        if(userType == User.UserType.CUSTOMER) {
            userValues.put("company_id", User.companyNameToId(userRecord.get("company_name")));
        } else {
            userValues.put("company_id", "");
        }
        userValues.put("address", userRecord.get("address"));
        userValues.put("type", userType.toString());

        return new User(userValues);
    }
}
package com.wama.backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    public static void main(String[] args) {
        System.out.println("Server started.");

        // Create database tables
        DatabaseManager.createDatabase();

        // Select all records from Users table
        String[] columns = {"id", "username", "email"};
        ArrayList<HashMap<String, String>> results = DatabaseManager.selectRecords("Users", columns, null);
        System.out.println("Users table records: " + results);
    }
}
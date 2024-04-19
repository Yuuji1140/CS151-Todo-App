package com.wama.backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseManager extends LogClass {
    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, email TEXT UNIQUE)";
    private static final String CREATE_USER_PASSWORDS_TABLE = "CREATE TABLE IF NOT EXISTS UserPasswords (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER UNIQUE, password TEXT, FOREIGN KEY (user_id) REFERENCES Users(id))";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found.");
            error("SQLite JDBC driver not found." , e);
        }
    }

    // TODO: Sanitize inputs methods

    public static void createDatabase() {

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // Query number of tables. if > 1, database already exists
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "Users", null);
            if (tables.next()) {
                System.out.println("Database already exists.");
                return;
            }
            stmt.execute(CREATE_USERS_TABLE);
            stmt.execute(CREATE_USER_PASSWORDS_TABLE);
            System.out.println("Database created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating database: " + e.getMessage());
        }
    }

    public static boolean insertRecord(String tableName, String[] columns, String[] values) {
        String insertQuery = "INSERT INTO " + tableName + " (" + String.join(", ", columns) + ") VALUES ('" + String.join("', '", values) + "')";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(insertQuery);
            System.out.println("Record inserted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting record: " + e.getMessage());
            return false;
        }
    }

    public static ArrayList<HashMap<String, String>> selectRecords(String tableName, String[] columns, String condition) {
        ArrayList<HashMap<String, String>> return_list = new ArrayList<>();

        String selectQuery = "SELECT " + String.join(", ", columns) + " FROM " + tableName;
        if (condition != null && !condition.isEmpty()) {
            selectQuery += " WHERE " + condition;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {
            // Parse the ResultSet and return a HashMap
            while(rs.next()) {
                HashMap<String, String> records = new HashMap<>();
                for (String column : columns) {
                    records.put(column, rs.getString(column));
                }
                return_list.add(records);
            }
            //return rs;
            return return_list;
        } catch (SQLException e) {
            System.out.println("Error selecting records: " + e.getMessage());
            return null;
        }
    }

    public static void updateRecord(String tableName, String[] columns, String[] values, String condition) {
        String updateQuery = "UPDATE " + tableName + " SET ";
        for (int i = 0; i < columns.length; i++) {
            updateQuery += columns[i] + " = '" + values[i] + "'";
            if (i < columns.length - 1) {
                updateQuery += ", ";
            }
        }
        if (condition != null && !condition.isEmpty()) {
            updateQuery += " WHERE " + condition;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(updateQuery);
            System.out.println("Record updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    public static void deleteRecord(String tableName, String condition) {
        String deleteQuery = "DELETE FROM " + tableName;
        if (condition != null && !condition.isEmpty()) {
            deleteQuery += " WHERE " + condition;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(deleteQuery);
            System.out.println("Record deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }
}
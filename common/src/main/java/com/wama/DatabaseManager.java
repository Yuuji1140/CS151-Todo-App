package com.wama;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseManager extends com.wama.LogClass {
    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS Users (" +
            "id TEXT PRIMARY KEY," +
            "username TEXT UNIQUE NOT NULL," +
            "email TEXT UNIQUE NOT NULL" +
            ")";

    private static final String CREATE_USER_PASSWORDS_TABLE = "CREATE TABLE IF NOT EXISTS UserPasswords (" +
            "user_id TEXT PRIMARY KEY," +
            "password TEXT NOT NULL," +
            "FOREIGN KEY (user_id) REFERENCES Users(id)" +
            ")";
    private static final String CREATE_AUTH_TOKENS_TABLE = "CREATE TABLE IF NOT EXISTS AuthTokens (" +
            "user_id TEXT PRIMARY KEY," +
            "auth_token TEXT UNIQUE NOT NULL," +
            "expires_at TIMESTAMP NOT NULL," +
            "FOREIGN KEY (user_id) REFERENCES Users(id)" +
            ")";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            error("SQLite JDBC driver not found.", e);
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
                debug("Database already exists.");
                return;
            }
            stmt.execute(CREATE_USERS_TABLE);
            stmt.execute(CREATE_USER_PASSWORDS_TABLE);
            stmt.execute(CREATE_AUTH_TOKENS_TABLE);
            info("Database created successfully.");
        } catch (SQLException e) {
            error("Error creating database: " + e.getMessage(), e);
        }
    }

    public static boolean insertRecord(String tableName, String[] columns, String[] values) {
        String insertQuery = "INSERT INTO " + tableName + " (" + String.join(", ", columns) + ") VALUES ('" + String.join("', '", values) + "')";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(insertQuery);
            debug("Record successfully inserted into " + tableName);
            return true;
        } catch (SQLException e) {
            error("Error at insertRecord: " + e.getMessage(), e);
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
            debug("Selected " + return_list.size() + " records from " + tableName);
            return return_list;
        } catch (SQLException e) {
            error("Error at selectRecords: " + e.getMessage(), e);
            return null;
        }
    }

    public static boolean updateRecord(String tableName, String[] columns, String[] values, String condition) {
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
            debug("Record in " + tableName + " updated successfully.");
            return true;
        } catch (SQLException e) {
            error("Error updating record: " + e.getMessage(), e);
            return false;
        }
    }

    public static boolean deleteRecord(String tableName, String condition) {
        String deleteQuery = "DELETE FROM " + tableName;
        if (condition != null && !condition.isEmpty()) {
            deleteQuery += " WHERE " + condition;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(deleteQuery);
            warn("Record in " + tableName + "matching" + condition + " deleted successfully.");
            return true;
        } catch (SQLException e) {
            error("Error deleting record: " + e.getMessage(), e);
            return false;
        }
    }
}
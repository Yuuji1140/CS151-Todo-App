package com.wama;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseManager extends com.wama.LogClass {
    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE IF NOT EXISTS Customers (" +
            "id TEXT PRIMARY KEY," +
            "company_name TEXT UNIQUE NOT NULL," +
            "phone TEXT," +
            "address TEXT," +
            ")";

    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS Users (" +
            "id TEXT PRIMARY KEY," +
            "username TEXT UNIQUE NOT NULL," +
            "email TEXT UNIQUE NOT NULL," +
            "user_type TEXT NOT NULL," +
            "company_name TEXT," + // NULL = Employee, NOT NULL = Customer
            "name TEXT NOT NULL," +
            "phone TEXT," +
            "address TEXT," +
            "FOREIGN KEY (company_name) REFERENCES Customers(company_name) ON DELETE CASCADE ON UPDATE CASCADE" +
            ")";

    private static final String CREATE_USER_PASSWORDS_TABLE = "CREATE TABLE IF NOT EXISTS UserPasswords (" +
            "user_id TEXT PRIMARY KEY," +
            "password TEXT NOT NULL," +
            "FOREIGN KEY (user_id) REFERENCES Users(id)" +
            ")";

    private static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS Products (" +
            "id TEXT PRIMARY KEY," +
            "name TEXT UNIQUE NOT NULL," +
            "description TEXT," +
            "price REAL NOT NULL," +
            "reorder_point INTEGER," +
            "current_stock INTEGER NOT NULL," +
            "encoded_image TEXT UNIQUE NOT NULL" +
            ")";

    private static final String CREATE_ORDERS_TABLE = "CREATE TABLE IF NOT EXISTS Orders (" +
            "id TEXT PRIMARY KEY," +
            "customer_id TEXT NOT NULL," +
            "order_date TIMESTAMP NOT NULL," +
            "status TEXT NOT NULL," +
            "total REAL NOT NULL," +
            "FOREIGN KEY (customer_id) REFERENCES Customers(id)," +
            ")";

    private static final String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS OrderItems (" +
            "id TEXT PRIMARY KEY," +
            "order_id TEXT NOT NULL," +
            "product_id TEXT NOT NULL," +
            "quantity INTEGER NOT NULL," +
            "price REAL NOT NULL," +
            "FOREIGN KEY (order_id) REFERENCES Orders(id)," +
            "FOREIGN KEY (product_id) REFERENCES Products(id)" +
            ")";

    private static final String CREATE_SHIPMENTS_TABLE = "CREATE TABLE IF NOT EXISTS Shipments (" +
            "id TEXT PRIMARY KEY," +
            "order_id TEXT NOT NULL," +
            "shipment_date TIMESTAMP NOT NULL," +
            "status TEXT NOT NULL," +
            "tracking_number TEXT," +
            "FOREIGN KEY (order_id) REFERENCES Orders(id)" +
            ")";

    private static final String[] TABLES = {
            CREATE_USERS_TABLE,
            CREATE_CUSTOMERS_TABLE,
            CREATE_USER_PASSWORDS_TABLE,
            CREATE_PRODUCTS_TABLE,
            CREATE_ORDERS_TABLE,
            CREATE_ORDER_ITEMS_TABLE,
            CREATE_SHIPMENTS_TABLE
    };

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
            // Create tables
            for (String TABLE_CREATION_STATEMENT : TABLES) {
                stmt.execute(TABLE_CREATION_STATEMENT);
            }

            info("Database created successfully.");
        } catch (SQLException e) {
            error("Error creating database: " + e.getMessage(), e);
        }
    }

    public static synchronized boolean insertRecord(String tableName, String[] columns, String[] values) {
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

    public static synchronized ArrayList<HashMap<String, String>> selectRecords(String tableName, String[] columns, String condition) {
        ArrayList<HashMap<String, String>> returnList = new ArrayList<>();

        String selectQuery = "SELECT " + String.join(", ", columns) + " FROM " + tableName;
        if (condition != null && !condition.isEmpty()) {
            selectQuery += " WHERE " + condition;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {

            while (rs.next()) {
                HashMap<String, String> records = new HashMap<>();
                for (String column : columns) {
                    records.put(column, rs.getString(column));
                }
                returnList.add(records);
            }

            debug("Selected " + returnList.size() + " records from " + tableName);
            return returnList;

        } catch (SQLException e) {
            error("Error at selectRecords: " + e.getMessage(), e);
            // Return an empty ArrayList instead of null
            return new ArrayList<>();
        }
    }

    public static synchronized boolean updateRecord(String tableName, String[] columns, String[] values, String condition) {
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

    public static synchronized boolean deleteRecord(String tableName, String condition) {
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

    public static synchronized ArrayList<HashMap<String, String>> getCustomerShipments(String customerId) {
        String query = "SELECT Shipments.id, Shipments.order_id, Shipments.shipment_date, Shipments.status, Shipments.tracking_number " +
                "FROM Shipments " +
                "JOIN Orders ON Shipments.order_id = Orders.id " +
                "WHERE Orders.customer_id = '" + customerId + "'";

        ArrayList<HashMap<String, String>> resultList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                HashMap<String, String> record = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    String columnValue = rs.getString(i);
                    record.put(columnName, columnValue);
                }
                resultList.add(record);
            }

            debug("Raw query executed successfully. Returned " + resultList.size() + " records.");
            return resultList;

        } catch (SQLException e) {
            error("Error executing raw query: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
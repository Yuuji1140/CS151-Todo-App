package com.wama;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Order extends LogClass {
    private final String id;
    private String customerId;
    private String employeeId;
    private Timestamp orderDate;
    private String status;
    private double total;
    private OrderItem[] items;
    /*
     * We can either create an order with just an ID and have it return an order
     * object with the rest of
     * the fields filled in, or we can use a static method to create an order and
     * generate a random ID for it.
     */

    public Order(String id) {
        this.id = id;
        // TODO: Fetch the rest of the fields from the database
        selectOrder(id);
    }

    private Order(String customerId, String employeeId, Timestamp orderDate, String status, double total) {
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.orderDate = orderDate;
        this.status = status;
        this.total = total;
    }

    private void selectOrder(String id2) {
        String[] columns = { "id", "customer_id", "employee_id", "order_date", "status", "total" };
        ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("Orders", columns,
                "id = '" + id + "'");
        if (result != null && !result.isEmpty()) {
            HashMap<String, String> order = result.get(0);

            this.customerId = order.get("customer_id");
            this.employeeId = order.get("employee_id");
            this.orderDate = Timestamp.valueOf(order.get("order_date"));
            this.status = order.get("status");
            this.total = Double.parseDouble(order.get("total"));
        } else {
            error("Order not found.");
            throw new IllegalArgumentException("Order not found");
        }
    }

    public Order createOrder(String customerId, String employeeId, Timestamp orderDate, String status,
            double total) {
        Order newOrder = new Order(customerId, employeeId, orderDate, status, total);
        // TODO: Insert the new order into the database
        try {
            DatabaseManager.insertRecord("Orders",
                    new String[] { "id", "customer_id", "employee_id", "order_date", "status", "total" },
                    new String[] { id, customerId, employeeId, orderDate.toString(), status, String.valueOf(total) });
        } catch (Exception e) {
            error("Error creating order: " + e.getMessage(), e);
            newOrder.deleteOrder();
            throw new IllegalArgumentException("Failed to create order.");
        }

        return newOrder;
    }

    public Order updateOrder(String customerId, String employeeId, Timestamp orderDate, String status,
            double total) {
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.orderDate = orderDate;
        this.status = status;
        this.total = total;

        try {
            DatabaseManager.updateRecord("Orders",
                    new String[] { "id", "customer_id", "employee_id", "order_date", "status", "total" },
                    new String[] { id, customerId, employeeId, orderDate.toString(), status, String.valueOf(total) },
                    "id = '" + id + "'");
        } catch (Exception e) {
            error("Error updating order: " + e.getMessage(), e);
            throw new IllegalArgumentException("Failed to update order.");
        }

        return this;
    }

    public void deleteOrder() {
        try {
            DatabaseManager.deleteRecord("Orders", "id = '" + id + "'");
        } catch (Exception e) {
            error("Error deleting order: " + e.getMessage(), e);
            throw new IllegalArgumentException("Failed to delete order.");
        }
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public double getTotal() {
        return total;
    }

    public OrderItem[] getItems() {
        return items;
    }

    // TODO: Setters and getters (change status, update total, etc.)
}
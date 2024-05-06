package com.wama;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Order extends LogClass {
    private final String id;
    private String customerId;
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
        if (!id.equalsIgnoreCase("all"))
            selectOrder();
    }

    private Order(String customerId, Timestamp orderDate, String status, double total) {
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.status = status;
        this.total = total;
    }

    public HashMap<String, String> selectOrder() {
        String[] columns = { "id", "customer_id", "order_date", "status", "total" };
        ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("Orders", columns,
                "id = '" + id + "'");
        if (result != null && !result.isEmpty()) {
            HashMap<String, String> order = result.get(0);

            this.customerId = order.get("customer_id");
            this.orderDate = Timestamp.valueOf(order.get("order_date"));
            this.status = order.get("status");
            this.total = Double.parseDouble(order.get("total"));
            return order;
        } else {
            error("Order not found.");
            throw new IllegalArgumentException("Order not found.");
        }
    }

    public static Order createOrder(String customerId, String orderDate, String status, double total) {
        Order newOrder = new Order(customerId, Timestamp.valueOf(orderDate), status, total);
        newOrder.insertOrder();
        return newOrder;
    }

    private void insertOrder() {
        try {
            DatabaseManager.insertRecord("Orders",
                    new String[] { "id", "customer_id", "order_date", "status", "total" },
                    new String[] { id, customerId, orderDate.toString(), status, String.valueOf(total) });
        } catch (Exception e) {
            error("Error creating order: " + e.getMessage(), e);
            deleteOrder();
            throw new IllegalArgumentException("Failed to create order.");
        }
    }

    public Order updateOrder(String customerId, String orderDate, String status,
            double total) {
        this.customerId = customerId;
        this.orderDate = Timestamp.valueOf(orderDate);
        this.status = status;
        this.total = total;

        try {
            DatabaseManager.updateRecord("Orders",
                    new String[] { "id", "customer_id", "order_date", "status", "total" },
                    new String[] { id, customerId, orderDate, status, String.valueOf(total) },
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

    public static ArrayList<HashMap<String, String>> getAllOrders() {
        return DatabaseManager.selectRecords("Orders",
                new String[] { "id", "customer_id", "order_date", "status", "total" },
                null);
    }

    public HashMap<String, String> getParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("customer_id", customerId);
        parameters.put("order_date", orderDate.toString());
        parameters.put("status", status);
        parameters.put("total", Double.toString(total));
        return parameters;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
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
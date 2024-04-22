package com.wama;

import java.sql.Timestamp;
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
    We can either create an order with just an ID and have it return an order object with the rest of
    the fields filled in, or we can use a static method to create an order and generate a random ID for it.
     */

    public Order(String id) {
        this.id = id;
        // TODO: Fetch the rest of the fields from the database
    }

    private Order(String customerId, String employeeId, Timestamp orderDate, String status, double total) {
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.orderDate = orderDate;
        this.status = status;
        this.total = total;
    }

    public static Order createOrder(String customerId, String employeeId, Timestamp orderDate,
                                    String status, double total) {
        Order newOrder = new Order(customerId, employeeId, orderDate, status, total);
        // TODO: Insert the new order into the database

        return newOrder;
    }

    // TODO: Setters and getters (change status, update total, etc.)
}
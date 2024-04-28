package com.wama;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.xml.crypto.Data;

public class OrderItem extends LogClass {
    private final String id;
    private String orderId;
    private String productId;
    private int quantity;
    private double price;

    public OrderItem(String id) {
        this.id = id;
        selectOrderItem();
    }

    private OrderItem(String orderId, String productId, int quantity, double price) {
        this.id = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        // TODO: Get fields from the database
    }

    public void selectOrderItem() {
        String[] columns = { "id", "order_id", "product_id", "quantity", "price" };
        ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("OrderItems", columns,
                "id = '" + id + "'");
        if (result != null && !result.isEmpty()) {
            HashMap<String, String> item = result.get(0);

            this.orderId = item.get("order_id");
            this.productId = item.get("product_id");
            this.quantity = Integer.parseInt(item.get("quantity"));
            this.price = Double.parseDouble(item.get("price"));
        } else {
            error("Item not found.");
            throw new IllegalArgumentException("Item not found.");
        }
    }

    public OrderItem createOrderItem(String orderId, String productId, int quantity, double price) {
        OrderItem newOrderItem = new OrderItem(orderId, productId, quantity, price);
        try {
            DatabaseManager.insertRecord("OrderItems",
                    new String[] { "id", "order_id", "product_id", "quantity", "price" },
                    new String[] { id, orderId, productId, Integer.toString(quantity), Double.toString(price) });
        } catch (Exception e) {
            error("Error creating item: " + e.getMessage(), e);
            newOrderItem.deleteOrderItem();
            throw new IllegalArgumentException("Failed to create item.");
        }

        return newOrderItem;
    }

    public OrderItem updateOrderItem(String orderId, String productId, int quantity, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;

        try {
            DatabaseManager.updateRecord("OrderItems",
                    new String[] { "id", "order_id", "product_id", "quantity", "price" },
                    new String[] { id, orderId, productId, Integer.toString(quantity), Double.toString(price) },
                    "id = '" + id + "'");
        } catch (Exception e) {
            error("Error updating item: " + e.getMessage(), e);
            throw new IllegalArgumentException("Failed to update item.");
        }

        return this;
    }

    public void deleteOrderItem() {
        try {
            DatabaseManager.deleteRecord("OrderItems", "id = '" + id + "'");
        } catch (Exception e) {
            error("Error deleting item: " + e.getMessage(), e);
            throw new IllegalArgumentException("Failed to delete item.");
        }
    }

    public OrderItem getOrderItem() {
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

}
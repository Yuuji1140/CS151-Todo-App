package com.wama;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class OrderItem extends LogClass {
    private final String id;
    private String orderId;
    private String productId;
    private Integer quantity;
    private Double price;

    public OrderItem(String id) {
        this.id = id;
        selectOrderItem();
    }

    private OrderItem(String orderId, String productId, Integer quantity, Double price) {
        this.id = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        // TODO: Get fields from the database
    }

    public HashMap<String, String> selectOrderItem() {
        ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("OrderItems",
                new String[] { "id", "order_id", "product_id", "quantity", "price" },
                "id = '" + id + "'");
        if (result != null && !result.isEmpty()) {
            HashMap<String, String> item = result.get(0);

            this.orderId = item.get("order_id");
            this.productId = item.get("product_id");
            this.quantity = (item.get("quantity") != null) ? Integer.parseInt(item.get("quantity")) : null;
            this.price = (item.get("price") != null) ? Double.parseDouble(item.get("price")) : null;
            return item;
        } else {
            error("Item not found.");
            throw new IllegalArgumentException("Item not found.");
        }
    }

    public static OrderItem createOrderItem(String orderId, String productId, Integer quantity, Double price) {
        OrderItem newOrderItem = new OrderItem(orderId, productId, quantity, price);

        String quantityString = (quantity != null) ? Double.toString(quantity) : null;
        String priceString = (price != null) ? Double.toString(price) : null;

        newOrderItem.insertOrderItem(quantityString, priceString);
        return newOrderItem;
    }

    private void insertOrderItem(String quantityString, String priceString) {
        try {
            DatabaseManager.insertRecord("OrderItems",
                    new String[] { "id", "order_id", "product_id", "quantity", "price" },
                    new String[] { id, orderId, productId, quantityString, priceString });
        } catch (Exception e) {
            error("Error creating item: " + e.getMessage(), e);
            deleteOrderItem();
            throw new IllegalArgumentException("Failed to create item.");
        }
    }

    public OrderItem updateOrderItem(String orderId, String productId, Integer quantity, Double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;

        String quantityString = (quantity != null) ? Double.toString(quantity) : null;
        String priceString = (price != null) ? Double.toString(price) : null;

        try {
            DatabaseManager.updateRecord("OrderItems",
                    new String[] { "id", "order_id", "product_id", "quantity", "price" },
                    new String[] { id, orderId, productId, quantityString, priceString },
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

    public static ArrayList<HashMap<String, String>> getAllOrderItems() {
        return DatabaseManager.selectRecords("OrderItems",
                new String[] { "id", "order_id", "product_id", "quantity", "price" },
                null);
    }

    public HashMap<String, String> getParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("order_id", orderId);
        parameters.put("product_id", productId);
        parameters.put("quantity", Integer.toString(quantity));
        parameters.put("price", Double.toString(price));
        return parameters;
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
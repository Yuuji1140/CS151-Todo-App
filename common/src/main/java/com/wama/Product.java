package com.wama;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Product extends LogClass {
    private final String id;
    private String name;
    private String description;
    private double price;
    private int reorderPoint;
    private int currentStock;
    /*
     * We can either create a product with just an ID and have it return a product
     * object with the rest of
     * the fields filled in, or we can use a static method to create a product with
     * all the fields filled in and
     * generate a random ID for it.
     */

    public Product(String id) {
        this.id = id;
        // TODO: Fetch the rest of the fields from the database
        selectProduct();
    }

    private Product(String name, String description, double price, int reorderPoint, int currentStock) {
        // Cryptographically secure random UUID -
        // shouldn't collide (if it does, we write a paper on it and get famous)
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.reorderPoint = reorderPoint;
        this.currentStock = currentStock;
    }

    public void selectProduct() {
        String[] columns = { "id", "name", "description", "price", "reorder_point", "current_stock" };
        ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("OrderItems", columns,
                "id = '" + id + "'");
        if (result != null && !result.isEmpty()) {
            HashMap<String, String> product = result.get(0);

            this.name = product.get("name");
            this.description = product.get("description");
            this.price = Double.parseDouble(product.get("price"));
            this.reorderPoint = Integer.parseInt(product.get("reorder_point"));
            this.currentStock = Integer.parseInt(product.get("current_stock"));
        } else {
            error("Product not found.");
            throw new IllegalArgumentException("Product not found.");
        }
    }

    public Product createProduct(String name, String description, double price,
            int reorderPoint, int currentStock) {
        Product newProduct = new Product(name, description, price, reorderPoint, currentStock);
        // TODO: Insert the new product into the database
        try {
            DatabaseManager.insertRecord("Product",
                    new String[] { "id", "name", "description", "price", "reorder_point", "current_stock" },
                    new String[] { id, name, description, Double.toString(price), Integer.toString(reorderPoint),
                            Integer.toString(currentStock)});
        } catch (Exception e) {
            error("Error creating product: " + e.getMessage(), e);
            newProduct.deleteProduct();
            throw new IllegalArgumentException("Failed to create product.");
        }
        return newProduct;
    }

    public Product updateProduct(String name, String description, double price,
            int reorderPoint, int currentStock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.reorderPoint = reorderPoint;
        this.currentStock = currentStock;

        try {
            DatabaseManager.updateRecord("Product",
                    new String[] { "id", "name", "description", "price", "reorder_point", "current_stock" },
                    new String[] { id, name, description, Double.toString(price), Integer.toString(reorderPoint),
                            Integer.toString(currentStock)}, "id = '" + id + "'");
        } catch (Exception e) {
            error("Error updating product: " + e.getMessage(), e);
            throw new IllegalArgumentException("Failed to update product.");
        }

        return this;
    }

    public void deleteProduct() {
        try {
            DatabaseManager.deleteRecord("Product", "id = '" + id + "'");
        } catch (Exception e) {
            error("Error deleting product: " + e.getMessage(), e);
            throw new IllegalArgumentException("Failed to delete product.");
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getReorderPoint() {
        return reorderPoint;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    // TODO: Setters and getters (change name, add stock, change price, etc.)

}
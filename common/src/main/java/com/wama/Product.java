package com.wama;

import java.util.UUID;

public class Product extends LogClass {
    private final String id;
    private String name;
    private String description;
    private double price;
    private int reorderPoint;
    private int currentStock;
    /*
    We can either create a product with just an ID and have it return a product object with the rest of
    the fields filled in, or we can use a static method to create a product with all the fields filled in and
    generate a random ID for it.
     */

    public Product(String id) {
        this.id = id;
        // TODO: Fetch the rest of the fields from the database
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

    public static Product createProduct(String name, String description, double price,
                                        int reorderPoint, int currentStock) {
        Product newProduct = new Product(name, description, price, reorderPoint, currentStock);
        // TODO: Insert the new product into the database

        return newProduct;
    }

    // TODO: Setters and getters (change name, add stock, change price, etc.)

}
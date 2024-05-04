package com.wama;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Product extends LogClass {
    private final String id;
    private String name;
    private String description;
    private Double price;
    private Integer reorderPoint;
    private Integer currentStock;
    private String encodedImg;
    /*
     * We can either create a product with just an ID and have it return a product
     * object with the rest of
     * the fields filled in, or we can use a static method to create a product with
     * all the fields filled in and
     * generate a random ID for it.
     */

    public Product(String id) {
        this.id = id;
        if (!id.equals("all"))
            selectProduct();
    }

    private Product(String name, String description, Double price, Integer reorderPoint, Integer currentStock,
            String encodedImg) {
        // Cryptographically secure random UUID -
        // shouldn't collide (if it does, we write a paper on it and get famous)
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.reorderPoint = reorderPoint;
        this.currentStock = currentStock;
        this.encodedImg = encodedImg; // Base64 encoded image to display in the frontend
    }

    public HashMap<String, String> selectProduct() {
        String[] columns = { "id", "name", "description", "price", "reorder_point", "current_stock", "encoded_image" };
        ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("Products", columns,
                "id = '" + id + "'");
        if (result != null && !result.isEmpty()) {
            HashMap<String, String> product = result.get(0);

            this.name = product.get("name");
            this.description = product.get("description");
            this.price = (product.containsKey("price")) ? Double.parseDouble(product.get("price")) : null;
            this.reorderPoint = (product.containsKey("reorder_point")) ? Integer.parseInt(product.get("reorder_point"))
                    : null;
            this.currentStock = (product.containsKey("current_stock")) ? Integer.parseInt(product.get("current_stock"))
                    : null;
            this.encodedImg = product.get("encoded_image");
            return product;
        } else {
            error("Product not found.");
            throw new IllegalArgumentException("Product not found.");
        }
    }

    public Product createProduct(String name, String description, Double price, Integer reorderPoint,
            Integer currentStock, String encodedImg) {
        Product newProduct = new Product(name, description, price, reorderPoint, currentStock, encodedImg);

        String priceString = (price == null) ? null : Double.toString(price);
        String reorderPointString = (reorderPoint == null) ? null : Integer.toString(reorderPoint);
        String currentStockString = (reorderPoint == null) ? null : Integer.toString(currentStock);

        try {
            DatabaseManager.insertRecord("Products",
                    new String[] { "id", "name", "description", "price", "reorder_point", "current_stock",
                            "encoded_image" },
                    new String[] { id, name, description, priceString, reorderPointString, currentStockString,
                            encodedImg });
        } catch (Exception e) {
            error("Error creating product: " + e.getMessage(), e);
            newProduct.deleteProduct();
            throw new IllegalArgumentException("Failed to create product.");
        }
        return newProduct;
    }

    public Product updateProduct(String name, String description, Double price, Integer reorderPoint,
            Integer currentStock, String encodedImg) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.reorderPoint = reorderPoint;
        this.currentStock = currentStock;
        this.encodedImg = encodedImg;

        String priceString = (price == null) ? null : Double.toString(price);
        String reorderPointString = (reorderPoint == null) ? null : Integer.toString(reorderPoint);
        String currentStockString = (reorderPoint == null) ? null : Integer.toString(currentStock);

        try {
            DatabaseManager.updateRecord("Products",
                    new String[] { "id", "name", "description", "price", "reorder_point", "current_stock",
                            "encoded_image" },
                    new String[] { id, name, description, priceString, reorderPointString, currentStockString,
                            encodedImg },
                    "id = '" + id + "'");
        } catch (Exception e) {
            error("Error updating product: " + e.getMessage(), e);
            throw new IllegalArgumentException("Failed to update product.");
        }

        return this;
    }

    public void deleteProduct() {
        try {
            DatabaseManager.deleteRecord("Products", "id = '" + id + "'");
        } catch (Exception e) {
            error("Error deleting product: " + e.getMessage(), e);
            throw new IllegalArgumentException("Failed to delete product.");
        }
    }

    public static ArrayList<HashMap<String, String>> getAllProducts() {
        return DatabaseManager.selectRecords("Products",
                new String[] { "id", "name", "description", "price", "reorder_point", "current_stock",
                        "encoded_image" },
                null);
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

    public Double getPrice() {
        return price;
    }

    public Integer getReorderPoint() {
        return reorderPoint;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public String getEncodedImg() {
        return encodedImg;
    }

    // TODO: Setters and getters (change name, add stock, change price, etc.)

}
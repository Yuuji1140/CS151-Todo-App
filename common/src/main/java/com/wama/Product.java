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
        ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("Products",
                new String[] { "id", "name", "description", "price", "reorder_point", "current_stock",
                        "encoded_image" },
                "id = '" + id + "'");
        if (result != null && !result.isEmpty()) {
            HashMap<String, String> product = result.get(0);

            this.name = product.get("name");
            this.description = product.get("description");
            this.price = (product.get("price") != null) ? Double.parseDouble(product.get("price")) : null;
            this.reorderPoint = (product.get("reorder_point") != null) ? Integer.parseInt(product.get("reorder_point"))
                    : null;
            this.currentStock = (product.get("current_stock") != null) ? Integer.parseInt(product.get("current_stock"))
                    : null;
            this.encodedImg = product.get("encoded_image");
            return product;
        } else {
            error("Product not found.");
            throw new IllegalArgumentException("Product not found.");
        }
    }

    public static Product createProduct(String name, String description, Double price, Integer reorderPoint,
            Integer currentStock, String encodedImg) {
        Product newProduct = new Product(name, description, price, reorderPoint, currentStock, encodedImg);

        String priceString = (price != null) ? Double.toString(price) : null;
        String reorderPointString = (reorderPoint != null) ? Integer.toString(reorderPoint) : null;
        String currentStockString = (reorderPoint != null) ? Integer.toString(currentStock) : null;

        newProduct.insertProduct(priceString, reorderPointString, currentStockString);
        return newProduct;
    }

    private void insertProduct(String priceString, String reorderPointString, String currentStockString) {
        try {
            DatabaseManager.insertRecord("Products",
                    new String[] { "id", "name", "description", "price", "reorder_point", "current_stock",
                            "encoded_image" },
                    new String[] { id, name, description, priceString, reorderPointString, currentStockString,
                            encodedImg });
        } catch (Exception e) {
            error("Error creating product: " + e.getMessage(), e);
            deleteProduct();
            throw new IllegalArgumentException("Failed to create product.");
        }
    }

    public Product updateProduct(String name, String description, Double price, Integer reorderPoint,
            Integer currentStock, String encodedImg) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.reorderPoint = reorderPoint;
        this.currentStock = currentStock;
        this.encodedImg = encodedImg;

        String priceString = (price != null) ? Double.toString(price) : null;
        String reorderPointString = (reorderPoint != null) ? Integer.toString(reorderPoint) : null;
        String currentStockString = (reorderPoint != null) ? Integer.toString(currentStock) : null;

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

    public HashMap<String, String> getParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("name", name);
        parameters.put("description", description);
        parameters.put("reorder_point", Integer.toString(reorderPoint));
        parameters.put("current_stock", Double.toString(currentStock));
        parameters.put("encoded_image", encodedImg);
        return parameters;
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
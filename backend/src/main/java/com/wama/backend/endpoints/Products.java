package com.wama.backend.endpoints;

import com.wama.DatabaseManager;
import com.wama.Product;

import java.io.OutputStream;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Products extends com.wama.LogClass implements Endpoint {
    private Product product;

    @Override
    public boolean validParameters(Map<String, String> parameters) {
        if (parameters == null) {
            error("Parameters are null");
            return false;
        }

        if (parameters.containsKey("name"))
            return !parameters.get("name").isEmpty();
        if (parameters.containsKey("id"))
            return !parameters.get("id").isEmpty();
        return false;
    }

    public HttpResponse handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        /*
         * Request is to get an array of hashmaps of all products in the Products table.
         */
        String id = parameters.get("id");

        if (id.equalsIgnoreCase("all")) {
            return new HttpResponse(HttpStatus.OK, Product.getAllProducts());
        }

        product = new Product(id);
        HashMap<String, String> productDetails = product.selectProduct();
        if (productDetails != null)
            return new HttpResponse(HttpStatus.OK, productDetails);

        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("error", "Product not found");
        return new HttpResponse(HttpStatus.NOT_FOUND, arguments);

    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        /*
         * Request is to create a new product in the Products table.
         */
        String name = parameters.get("name");
        String description = parameters.get("description");
        double price = (parameters.get("price") != null) ? Double.parseDouble(parameters.get("price")) : null;
        int reorderPoint = (parameters.get("reorder_point") != null) ? Integer.parseInt(parameters.get("reorder_point"))
                : null;
        int initialStock = (parameters.get("rinitial_stock") != null)
                ? Integer.parseInt(parameters.get("initial_stock"))
                : null;
        String encodedImg = parameters.get("encoded_image");

        product = product.createProduct(name, description, price, reorderPoint, initialStock, encodedImg);
        if (product != null) {
            return new HttpResponse(HttpStatus.CREATED, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error creating product");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    public HttpResponse handlePutRequest(Map<String, String> parameters, OutputStream outputStream) {
        /*
         * Request is to update an existing product in the Products table.
         */
        String id = parameters.get("id");
        String name = parameters.get("name");
        String description = parameters.get("description");
        Double price = (parameters.get("price") != null) ? Double.parseDouble(parameters.get("price")) : null;
        Integer reorderPoint = (parameters.get("reorder_point") != null) ? Integer.parseInt(parameters.get("reorder_point"))
                : null;
        Integer initialStock = (parameters.get("rinitial_stock") != null)
                ? Integer.parseInt(parameters.get("initial_stock"))
                : null;
        String encodedImg = parameters.get("encoded_image");

        product = (id != null) ? new Product(id) : new Product(findId(name));

        Product updatedProduct = product.updateProduct(name, description, price, reorderPoint, initialStock,
                encodedImg);
        if (updatedProduct != null) {
            return new HttpResponse(HttpStatus.OK, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error updating product");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    public HttpResponse handleDeleteRequest(Map<String, String> parameters, OutputStream outputStream) {
        /*
         * Request is to delete an existing product in the Products table.
         */
        String id = parameters.get("id");

        product = new Product(id);
        product.deleteProduct();
        if (product == null) {
            return new HttpResponse(HttpStatus.OK, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error deleting product");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    private String findId(String name) {
        ArrayList<HashMap<String, String>> productRecords = DatabaseManager.selectRecords("Products",
                new String[] { "id", "name", "description", "price", "reorder_point", "current_stock",
                        "encoded_image" },
                "LOWER(name) = LOWER('" + name + "')");

        if (productRecords == null || productRecords.size() != 1) {
            return null;
        }

        HashMap<String, String> productRecord = productRecords.get(0);
        return productRecord.get("id");
    }
}

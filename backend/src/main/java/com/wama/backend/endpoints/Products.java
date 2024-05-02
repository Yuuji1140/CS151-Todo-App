package com.wama.backend.endpoints;

import com.wama.Product;

import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

public class Products extends com.wama.LogClass implements Endpoint {
    private Product product;

    @Override
    public boolean validParameters(Map<String, String> parameters) {
        if (parameters == null) {
            error("Parameters are null");
            return false;
        }
        String requestType = parameters.get("requestType");
        if (requestType.equals("POST") || requestType.equals("PUT")) {
            if (!parameters.containsKey("name") || !parameters.containsKey("description") ||
                    !parameters.containsKey("price") || !parameters.containsKey("reorder_point") ||
                    !parameters.containsKey("initial_stock")) {
                error("Parameters are missing for POST or PUT request");
                return false;
            }
        } else if (requestType.equals("DELETE") || requestType.equals("GET")) {
            if (!parameters.containsKey("id")) {
                error("ID parameter is missing for DELETE or GET request");
                return false;
            }
        }
        return true;
    }

    public HttpResponse handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        /*
        Request is to get an array of hashmaps of all products in the Products table.
         */
        String id = parameters.get("id");

        product = new Product(id);
        if (id.equalsIgnoreCase("all"))
            return new HttpResponse(HttpStatus.OK, com.wama.Product.getAllProducts());

        HashMap<String, String> productDetails = product.selectProduct();
        if (productDetails != null)
            return new HttpResponse(HttpStatus.OK, productDetails);

        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("error", "Product not found");
        return new HttpResponse(HttpStatus.NOT_FOUND, arguments);

    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        /*
        Request is to create a new product in the Products table.
         */
        String name = parameters.get("name");
        String description = parameters.get("description");
        double price = Double.parseDouble(parameters.get("price"));
        int reorderPoint = Integer.parseInt(parameters.get("reorder_point"));
        int initialStock = Integer.parseInt(parameters.get("initial_stock"));
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
        Request is to update an existing product in the Products table.
         */
        String id = parameters.get("id");
        String name = parameters.get("name");
        String description = parameters.get("description");
        double price = Double.parseDouble(parameters.get("price"));
        int reorderPoint = Integer.parseInt(parameters.get("reorder_point"));
        int initialStock = Integer.parseInt(parameters.get("initial_stock"));
        String encodedImg = parameters.get("encoded_image");

        product = new Product(id);
        Product updatedProduct = product.updateProduct(name, description, price, reorderPoint, initialStock, encodedImg);
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
        Request is to delete an existing product in the Products table.
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
}

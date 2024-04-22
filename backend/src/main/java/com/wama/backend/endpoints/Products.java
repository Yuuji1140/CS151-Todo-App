package com.wama.backend.endpoints;

import com.wama.DatabaseManager;

import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Products extends com.wama.LogClass implements Endpoint {

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
        String id = parameters.get("id");
        String[] columns = {"id", "name", "description", "price", "reorder_point", "current_stock"};
        if (id != null) {
            ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("Products", columns, "id = '" + id + "'");
            if (result.size() > 0) {
                HashMap<String, String> product = result.get(0);
                return new HttpResponse(HttpStatus.OK, product);
            } else {
                HashMap<String, String> arguments = new HashMap<>();
                arguments.put("error", "Product not found");
                return new HttpResponse(HttpStatus.NOT_FOUND, arguments);
            }
        } else {
            ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("Products", columns, null);
            return new HttpResponse(HttpStatus.OK, result);
        }
    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        String name = parameters.get("name");
        String description = parameters.get("description");
        double price = Double.parseDouble(parameters.get("price"));
        int reorderPoint = Integer.parseInt(parameters.get("reorder_point"));
        int initialStock = Integer.parseInt(parameters.get("initial_stock"));
        String[] columns = {"name", "description", "price", "reorder_point", "current_stock"};
        String[] values = {name, description, String.valueOf(price), String.valueOf(reorderPoint), String.valueOf(initialStock)};
        if (DatabaseManager.insertRecord("Products", columns, values)) {
            return new HttpResponse(HttpStatus.CREATED, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error creating product");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    public HttpResponse handlePutRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");
        String name = parameters.get("name");
        String description = parameters.get("description");
        double price = Double.parseDouble(parameters.get("price"));
        int reorderPoint = Integer.parseInt(parameters.get("reorder_point"));
        String[] columns = {"name", "description", "price", "reorder_point"};
        String[] values = {name, description, String.valueOf(price), String.valueOf(reorderPoint)};
        if (DatabaseManager.updateRecord("Products", columns, values, "id = '" + id + "'")) {
            return new HttpResponse(HttpStatus.OK, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error updating product");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    public HttpResponse handleDeleteRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");
        if (DatabaseManager.deleteRecord("Products", "id = '" + id + "'")) {
            return new HttpResponse(HttpStatus.OK, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error deleting product");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }
}
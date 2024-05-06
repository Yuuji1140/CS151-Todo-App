package com.wama.backend.endpoints;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.wama.OrderItem;

public class OrderItems extends com.wama.LogClass implements Endpoint {
    private OrderItem item;

    @Override
    public boolean validParameters(Map<String, String> parameters) {
        if (parameters == null) {
            error("Parameters are null");
            return false;
        }

        if (parameters.containsKey("order_id") && parameters.containsKey("product_id"))
            return !parameters.get("order_id").isEmpty() && !parameters.get("product_id").isEmpty();

        error("Parameters are missing.");
        return false;
    }

    public HttpResponse handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");

        if (id.equalsIgnoreCase("all")) {
            return new HttpResponse(HttpStatus.OK, OrderItem.getAllOrderItems());
        }

        item = new OrderItem(id);
        HashMap<String, String> itemDetails = item.selectOrderItem();
        if (itemDetails != null)
            return new HttpResponse(HttpStatus.OK, itemDetails);

        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("error", "Order item not found");
        return new HttpResponse(HttpStatus.NOT_FOUND, arguments);
    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        String orderId = parameters.get("order_id");
        String productId = parameters.get("product_id");
        Integer quantity = Integer.parseInt(parameters.get("quantity"));
        Double price = Double.parseDouble(parameters.get("price"));
        
        item = OrderItem.createOrderItem(orderId, productId, quantity, price);
        if (item != null) {
            // CREATED status is not being handled in frontend, swap to OK status for now
            // return new HttpResponse(HttpStatus.CREATED, new HashMap<>());
            return new HttpResponse(HttpStatus.OK, item.getParameters());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error creating product");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    public HttpResponse handlePutRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");
        String orderId = parameters.get("order_id");
        String productId = parameters.get("product_id");
        Integer quantity = Integer.parseInt(parameters.get("quantity"));
        Double price = Double.parseDouble(parameters.get("price"));

        item = new OrderItem(id);
        item.updateOrderItem(orderId, productId, quantity, price);
        if (item != null) {
            return new HttpResponse(HttpStatus.OK, item.getParameters());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error updating order");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    public HttpResponse handleDeleteRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");

        item = new OrderItem(id);
        item.deleteOrderItem();
        if (item == null) {
            return new HttpResponse(HttpStatus.OK, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error deleting order");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }
}

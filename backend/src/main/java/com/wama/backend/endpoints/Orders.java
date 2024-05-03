package com.wama.backend.endpoints;

import com.wama.Order;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Orders extends com.wama.LogClass implements Endpoint {
    private Order order;

    @Override
    public boolean validParameters(Map<String, String> parameters) {
        if (parameters == null) {
            error("Parameters are null");
            return false;
        }
        String requestType = parameters.get("requestType");
        if (requestType.equals("POST") || requestType.equals("PUT")) {
            if (!parameters.containsKey("customer_id") ||
                    !parameters.containsKey("order_date") || !parameters.containsKey("status") ||
                    !parameters.containsKey("total")) {
                error("Parameters are missing for POST or PUT request");
                return false;
            }
        } else if (requestType.equals("DELETE") || requestType.equals("GET")) {
            if (!parameters.containsKey("id")) {
                error("ID parameter is missing for DELETE or GET request");
                return false;
            }
            if (parameters.get("id").equalsIgnoreCase("all") && !parameters.containsKey("company_id")) {
                error("Company ID parameter is missing for GET request");
                return false;
            }
        } else {
            error("Invalid request type");
            return false;
        }
        return true;
    }

    public HttpResponse handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");
        if (id.equalsIgnoreCase("all") && parameters.containsKey("company_id")) {
            ArrayList<HashMap<String, String>> returnOrders = new ArrayList<>();
            ArrayList<HashMap<String, String>> orders = Order.getAllOrders();
            for (HashMap<String, String> order : orders) {
                if (order.get("customer_id").equals(parameters.get("company_id"))) {
                    returnOrders.add(order);
                }
            }
            return new HttpResponse(HttpStatus.OK, returnOrders);
        }

        order = new Order(id);
        HashMap<String, String> orderDetails = order.selectOrder();
        if (orderDetails != null) {
            return new HttpResponse(HttpStatus.OK, orderDetails);
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Order not found");
            return new HttpResponse(HttpStatus.NOT_FOUND, arguments);
        }

    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        String customerId = parameters.get("customer_id");
        String employeeId = parameters.get("employee_id");
        String orderDate = parameters.get("order_date");
        String status = parameters.get("status");
        double total = Double.parseDouble(parameters.get("total"));

        order = order.createOrder(customerId, employeeId, orderDate, status, total);
        if (order != null) {
            return new HttpResponse(HttpStatus.CREATED, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error creating order");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    public HttpResponse handlePutRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");
        String customerId = parameters.get("customer_id");
        String employeeId = parameters.get("employee_id");
        String orderDate = parameters.get("order_date");
        String status = parameters.get("status");
        double total = Double.parseDouble(parameters.get("total"));

        order = new Order(id);
        order.updateOrder(customerId, employeeId, orderDate, status, total);
        if (order != null) {
            return new HttpResponse(HttpStatus.OK, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error updating order");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    public HttpResponse handleDeleteRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");

        order = new Order(id);
        order.deleteOrder();
        if (order == null) {
            return new HttpResponse(HttpStatus.OK, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error deleting order");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }
}
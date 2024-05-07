package com.wama.backend.endpoints;

import com.wama.DatabaseManager;
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

        if (parameters.containsKey("customer_id"))
            return !parameters.get("customer_id").isEmpty();

        if (parameters.containsKey("customer_id") && parameters.containsKey("order_date"))
            return !parameters.get("customer_id").isEmpty() && !parameters.get("order_date").isEmpty();

        error("Parameters are missing.");
        return false;
    }

    public HttpResponse handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");
        if (id.equalsIgnoreCase("all") && parameters.containsKey("customer_id")) {
            ArrayList<HashMap<String, String>> returnOrders = new ArrayList<>();
            ArrayList<HashMap<String, String>> orders = Order.getAllOrders();
            for (HashMap<String, String> order : orders) {
                if (order.get("customer_id").equals(parameters.get("customer_id"))) {
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
        String orderDate = parameters.get("order_date");
        String status = parameters.get("status");
        double total = Double.parseDouble(parameters.get("total"));

        order = Order.createOrder(customerId, orderDate, status, total);
        if (order != null) {
            // CREATED status is not being handled in frontend, swap to OK status for now
            // return new HttpResponse(HttpStatus.CREATED, new HashMap<>());
            return new HttpResponse(HttpStatus.OK, order.getParameters());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error creating order");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    public HttpResponse handlePutRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");
        String customerId = parameters.get("customer_id");
        String orderDate = parameters.get("order_date");

        order = (id != null) ? new Order(id) : new Order(findId(customerId, orderDate));

        String status = (parameters.get("status") != null) ? parameters.get("status") : order.getStatus();
        Double total = (parameters.get("total") != null) ? Double.parseDouble(parameters.get("total")) : order.getTotal();

        order = new Order(id);
        Order updatedOrder = order.updateOrder(customerId, orderDate, status, total);
        if (order != null) {
            return new HttpResponse(HttpStatus.OK, updatedOrder.getParameters());
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

    private String findId(String customerId, String orderDate) {
        ArrayList<HashMap<String, String>> orderRecords = DatabaseManager.selectRecords("Products",
                new String[] { "id", "customer_id", "order_date" },
                "customer_id = '" + customerId + "' AND order_date = '" + orderDate + "'");
        if (orderRecords == null || orderRecords.size() != 1) {
            return null;
        }

        return orderRecords.get(0).get("id");

    }
}
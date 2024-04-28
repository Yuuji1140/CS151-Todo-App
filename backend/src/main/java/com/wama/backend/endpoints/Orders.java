package com.wama.backend.endpoints;

import com.wama.Order;

import java.io.OutputStream;
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
            if (!parameters.containsKey("customer_id") || !parameters.containsKey("employee_id") ||
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
        }
        return true;
    }

    public HttpResponse handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");

        if (id != null) {
            order = new Order(id);
            HashMap<String, String> orderDetails = order.selectOrder();
            if (orderDetails != null) {
                return new HttpResponse(HttpStatus.OK, orderDetails);
            } else {
                HashMap<String, String> arguments = new HashMap<>();
                arguments.put("error", "Order not found");
                return new HttpResponse(HttpStatus.NOT_FOUND, arguments);
            }
        } else {
            return new HttpResponse(HttpStatus.OK, com.wama.Order.getAllOrders());
        }

        // String[] columns = { "id", "customer_id", "employee_id", "order_date", "status", "total" };
        // if (id != null) {
        //     ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("Orders", columns,
        //             "id = '" + id + "'");
        //     if (result.size() > 0) {
        //         HashMap<String, String> order = result.get(0);
        //         return new HttpResponse(HttpStatus.OK, order);
        //     } else {
        //         HashMap<String, String> arguments = new HashMap<>();
        //         arguments.put("error", "Order not found");
        //         return new HttpResponse(HttpStatus.NOT_FOUND, arguments);
        //     }
        // } else {
        //     ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("Orders", columns, null);
        //     return new HttpResponse(HttpStatus.OK, result);
        // }
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
        
        // String[] columns = { "customer_id", "employee_id", "order_date", "status", "total" };
        // String[] values = { customerId, employeeId, orderDate, status, String.valueOf(total) };
        // if (DatabaseManager.insertRecord("Orders", columns, values)) {
        //     return new HttpResponse(HttpStatus.CREATED, new HashMap<>());
        // } else {
        //     HashMap<String, String> arguments = new HashMap<>();
        //     arguments.put("error", "Error creating order");
        //     return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        // }
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

        // String[] columns = { "customer_id", "employee_id", "order_date", "status", "total" };
        // String[] values = { customerId, employeeId, orderDate, status, String.valueOf(total) };
        // if (DatabaseManager.updateRecord("Orders", columns, values, "id = '" + id + "'")) {
        //     return new HttpResponse(HttpStatus.OK, new HashMap<>());
        // } else {
        //     HashMap<String, String> arguments = new HashMap<>();
        //     arguments.put("error", "Error updating order");
        //     return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        // }
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
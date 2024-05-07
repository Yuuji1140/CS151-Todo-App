package com.wama.backend.endpoints;

import com.wama.DatabaseManager;
import com.wama.Shipment;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.*;

public class Shipments extends com.wama.LogClass implements Endpoint {
    @Override
    public boolean validParameters(Map<String, String> parameters) {
        if (parameters == null) {
            error("Parameters are null");
            return false;
        }

        if (parameters.containsKey("id"))
            return !parameters.get("id").isEmpty();

        if (parameters.containsKey("customer_id"))
            return !parameters.get("customer_id").isEmpty();

        if (parameters.containsKey("order_id") && parameters.containsKey("shipment_date") &&
                parameters.containsKey("status") && parameters.containsKey("tracking_number"))
            return !parameters.get("order_id").isEmpty() && !parameters.get("shipment_date").isEmpty() &&
                    !parameters.get("status").isEmpty() && !parameters.get("tracking_number").isEmpty();

        error("Parameters are missing.");
        return false;
    }

    public HttpResponse handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");

        if (id.equalsIgnoreCase("all")) {
            return new HttpResponse(HttpStatus.OK, getAllShipments());
        }
        
        if (parameters.containsKey("id")) {
            return new HttpResponse(HttpStatus.OK,
                    Objects.requireNonNull(getShipmentById(parameters.get("id"))).getParameters());
        } else if (parameters.containsKey("customer_id")) {
            return new HttpResponse(HttpStatus.OK, getShipmentsByCustomerId(parameters.get("customer_id")));
        } else {
            return new HttpResponse(HttpStatus.OK, getAllShipments());
        }
    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        String orderId = parameters.get("order_id");
        String shipmentDate = parameters.get("shipment_date");
        String status = parameters.get("status");
        String trackingNumber = parameters.get("tracking_number");

        Shipment shipment = createShipment(orderId, shipmentDate, status, trackingNumber);
        if (shipment != null) {
            return new HttpResponse(HttpStatus.CREATED, shipment.getParameters());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error creating shipment");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    public HttpResponse handlePutRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");
        String orderId = parameters.get("order_id");
        String shipmentDate = parameters.get("shipment_date");
        String status = parameters.get("status");
        String trackingNumber = parameters.get("tracking_number");

        Shipment shipment = updateShipment(id, orderId, shipmentDate, status, trackingNumber);
        if (shipment != null) {
            return new HttpResponse(HttpStatus.OK, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error updating shipment");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    public HttpResponse handleDeleteRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");

        if (deleteShipment(id)) {
            return new HttpResponse(HttpStatus.OK, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error deleting shipment");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    private Shipment createShipment(String orderId, String shipmentDate, String status, String trackingNumber) {
        String id = UUID.randomUUID().toString();
        Timestamp timestamp = Timestamp.valueOf(shipmentDate);
        Shipment shipment = new Shipment(id, orderId, timestamp, status, trackingNumber);
        try {
            DatabaseManager.insertRecord("Shipments",
                    new String[] { "id", "order_id", "shipment_date", "status", "tracking_number" },
                    new String[] { id, orderId, shipmentDate, status, trackingNumber });
            return shipment;
        } catch (Exception e) {
            error("Error creating shipment: " + e.getMessage(), e);
            return null;
        }
    }

    private Shipment getShipmentById(String id) {
        String[] columns = { "id", "order_id", "shipment_date", "status", "tracking_number" };
        ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("Shipments", columns,
                "id = '" + id + "'");
        if (!result.isEmpty()) {
            HashMap<String, String> shipmentMap = result.get(0);
            return new Shipment(shipmentMap);
        }
        return null;
    }

    private Shipment updateShipment(String id, String orderId, String shipmentDate, String status,
            String trackingNumber) {
        Timestamp timestamp = Timestamp.valueOf(shipmentDate);
        try {
            DatabaseManager.updateRecord("Shipments",
                    new String[] { "id", "order_id", "shipment_date", "status", "tracking_number" },
                    new String[] { id, orderId, shipmentDate, status, trackingNumber },
                    "id = '" + id + "'");
            return new Shipment(id, orderId, timestamp, status, trackingNumber);
        } catch (Exception e) {
            error("Error updating shipment: " + e.getMessage(), e);
            return null;
        }
    }

    private boolean deleteShipment(String id) {
        try {
            DatabaseManager.deleteRecord("Shipments", "id = '" + id + "'");
            return true;
        } catch (Exception e) {
            error("Error deleting shipment: " + e.getMessage(), e);
            return false;
        }
    }

    private ArrayList<HashMap<String, String>> getAllShipments() {
        return DatabaseManager.selectRecords("Shipments",
                new String[] { "id", "order_id", "shipment_date", "status", "tracking_number" },
                null);
    }

    public static ArrayList<HashMap<String, String>> getShipmentsByCustomerId(String customerId) {
        try {
            return DatabaseManager.getCustomerShipments(customerId);
        } catch (Exception e) {
            error("Error getting shipments by customer ID: " + e.getMessage(), e);
            throw new IllegalArgumentException("Failed to get shipments by customer ID.");
        }
    }
}
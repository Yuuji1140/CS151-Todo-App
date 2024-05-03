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
        String requestType = parameters.get("requestType");
        if (requestType.equals("POST") || requestType.equals("PUT")) {
            if (!parameters.containsKey("order_id") || !parameters.containsKey("shipment_date") ||
                    !parameters.containsKey("status") || !parameters.containsKey("tracking_number")) {
                error("Parameters are missing for POST or PUT request");
                return false;
            }
        } else if (requestType.equals("DELETE") || requestType.equals("GET")) {
            if (!parameters.containsKey("id") || !parameters.containsKey("company_id")) {
                error("Must have id or company_id for DELETE or GET request");
                return false;
            }
        }
        return true;
    }

    public HttpResponse handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        if (parameters.containsKey("id")) {
            return new HttpResponse(HttpStatus.OK,
                    Objects.requireNonNull(getShipmentById(parameters.get("id"))).getParameters());
        } else if (parameters.containsKey("company_id")) {
            return new HttpResponse(HttpStatus.OK, getShipmentsByCustomerId(parameters.get("company_id")));
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
            return new HttpResponse(HttpStatus.CREATED, new HashMap<>());
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

    private Shipment updateShipment(String id, String orderId, String shipmentDate, String status, String trackingNumber) {
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
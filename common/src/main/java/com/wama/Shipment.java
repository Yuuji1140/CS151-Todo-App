package com.wama;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Shipment extends LogClass {
    private final String id;
    private String orderId;
    private Timestamp shipmentDate;
    private String status;
    private String trackingNumber;

    public Shipment(String id) {
        // Select a single shipment
        this.id = id;
        selectShipment();
    }

    private Shipment(String orderId, Timestamp shipmentDate, String status, String trackingNumber) {
        // Create a new shipment
        this.id = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.shipmentDate = shipmentDate;
        this.status = status;
        this.trackingNumber = trackingNumber;
    }

    public Shipment(HashMap<String, String> shipmentValues) {
        // Unpack them from the frontend
        this.id = shipmentValues.get("id");
        this.orderId = shipmentValues.get("order_id");
        this.shipmentDate = Timestamp.valueOf(shipmentValues.get("shipment_date"));
        this.status = shipmentValues.get("status");
        this.trackingNumber = shipmentValues.get("tracking_number");
    }

    public HashMap<String, String> selectShipment() {
        String[] columns = { "id", "order_id", "shipment_date", "status", "tracking_number" };
        ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("Shipments", columns,
                "id = '" + id + "'");
        if (result != null && !result.isEmpty()) {
            HashMap<String, String> shipment = result.get(0);
            this.orderId = shipment.get("order_id");
            this.shipmentDate = Timestamp.valueOf(shipment.get("shipment_date"));
            this.status = shipment.get("status");
            this.trackingNumber = shipment.get("tracking_number");
            return shipment;
        } else {
            error("Shipment not found.");
            throw new IllegalArgumentException("Shipment not found.");
        }
    }

    public Shipment createShipment(String orderId, String shipmentDate, String status,
            String trackingNumber) {
        Shipment newShipment = new Shipment(orderId, Timestamp.valueOf(shipmentDate), status, trackingNumber);
        try {
            DatabaseManager.insertRecord("Shipments",
                    new String[] { "id", "order_id", "shipment_date", "status", "tracking_number" },
                    new String[] { id, orderId, shipmentDate, status, trackingNumber });
        } catch (Exception e) {
            error("Error creating shipment: " + e.getMessage(), e);
            newShipment.deleteShipment();
            throw new IllegalArgumentException("Failed to create shipment.");
        }

        return newShipment;
    }

    public Shipment updateShipment(String orderId, String shipmentDate, String status,
            String trackingNumber) {
        this.orderId = orderId;
        this.shipmentDate = Timestamp.valueOf(shipmentDate);
        this.status = status;
        this.trackingNumber = trackingNumber;

        try {
            DatabaseManager.updateRecord("Shipments",
                    new String[] { "id", "order_id", "shipment_date", "status", "tracking_number" },
                    new String[] { id, orderId, shipmentDate, status, trackingNumber },
                    "id = '" + id + "'");
        } catch (Exception e) {
            error("Error updating shipment: " + e.getMessage(), e);
            throw new IllegalArgumentException("Failed to update shipment.");
        }
        return this;
    }

    public void deleteShipment() {
        try {
            DatabaseManager.deleteRecord("Shipments", "id = '" + id + "'");
        } catch (Exception e) {
            error("Error deleting shipment: " + e.getMessage(), e);
            throw new IllegalArgumentException("Failed to delete shipment.");
        }
    }

    public static ArrayList<HashMap<String, String>> getShipmentsByCustomerId(String customerId) {
        try {
            return DatabaseManager.getCustomerShipments(customerId);
        } catch (Exception e) {
            error("Error getting shipments by customer ID: " + e.getMessage(), e);
            throw new IllegalArgumentException("Failed to get shipments by customer ID.");
        }
    }

    public static ArrayList<HashMap<String, String>> getAllShipments() {
        return DatabaseManager.selectRecords("Shipments",
                new String[] { "id", "order_id", "shipment_date", "status", "tracking_number" },
                null);
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public Timestamp getShipmentDate() {
        return shipmentDate;
    }

    public String getStatus() {
        return status;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

}
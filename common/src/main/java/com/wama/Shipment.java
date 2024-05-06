package com.wama;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

public class Shipment extends LogClass {
    private final String id;
    private String orderId;
    private Timestamp shipmentDate;
    private String status;
    private String trackingNumber;

    public Shipment(String id, String orderId, Timestamp shipmentDate, String status, String trackingNumber) {
        this.id = id;
        this.orderId = orderId;
        this.shipmentDate = shipmentDate;
        this.status = status;
        this.trackingNumber = trackingNumber;
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

    public HashMap<String, String> getParameters() {
        HashMap<String, String> shipment = new HashMap<>();
        shipment.put("id", id);
        shipment.put("order_id", orderId);
        shipment.put("shipment_date", shipmentDate.toString());
        shipment.put("status", status);
        shipment.put("tracking_number", trackingNumber);
        return shipment;
    }

}
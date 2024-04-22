package com.wama;

import java.sql.Timestamp;
import java.util.UUID;

public class Shipment extends LogClass {
    private final String id;
    private String orderId;
    private Timestamp shipmentDate;
    private String status;
    private String trackingNumber;

    public Shipment(String id) {
        this.id = id;
        // TODO: Fetch the rest of the fields from the database
    }

    private Shipment(String orderId, Timestamp shipmentDate, String status, String trackingNumber) {
        this.id = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.shipmentDate = shipmentDate;
        this.status = status;
        this.trackingNumber = trackingNumber;
    }

    public static Shipment createShipment(String orderId, Timestamp shipmentDate, String status,
                                          String trackingNumber) {
        Shipment newShipment = new Shipment(orderId, shipmentDate, status, trackingNumber);
        // TODO: Insert the new shipment into the database

        return newShipment;
    }

    // TODO: Setters and getters (change status, update tracking number, etc.)
}
package com.wama.backend.endpoints;

import com.wama.Shipment;

import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

public class Shipments extends com.wama.LogClass implements Endpoint {
    private Shipment shipment;

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
            shipment = new Shipment(id);
            HashMap<String, String> shipmentDetails = shipment.selectShipment();
            if (shipmentDetails != null) {
                return new HttpResponse(HttpStatus.OK, shipmentDetails);
            }  else {
                HashMap<String, String> arguments = new HashMap<>();
                arguments.put("error", "Shipment not found");
                return new HttpResponse(HttpStatus.NOT_FOUND, arguments);
            }
        } else {
            return new HttpResponse(HttpStatus.OK, com.wama.Shipment.getAllShipments());
        }
    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        String orderId = parameters.get("order_id");
        String shipmentDate = parameters.get("shipment_date");
        String status = parameters.get("status");
        String trackingNumber = parameters.get("tracking_number");

        shipment = shipment.createShipment(orderId, shipmentDate, status, trackingNumber);
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
        
        shipment = new Shipment(id);
        shipment.updateShipment(orderId, shipmentDate, status, trackingNumber);
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
        
        shipment = new Shipment(id);
        shipment.deleteShipment();
        if (shipment == null) {
            return new HttpResponse(HttpStatus.OK, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error deleting shipment");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }
}
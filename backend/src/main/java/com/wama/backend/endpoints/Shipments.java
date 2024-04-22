package com.wama.backend.endpoints;

import com.wama.DatabaseManager;

import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

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
            if (!parameters.containsKey("id")) {
                error("ID parameter is missing for DELETE or GET request");
                return false;
            }
        }
        return true;
    }

    public HttpResponse handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");
        String[] columns = {"id", "order_id", "shipment_date", "status", "tracking_number"};
        if (id != null) {
            ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("Shipments", columns, "id = '" + id + "'");
            if (result.size() > 0) {
                HashMap<String, String> shipment = result.get(0);
                return new HttpResponse(HttpStatus.OK, shipment);
            } else {
                HashMap<String, String> arguments = new HashMap<>();
                arguments.put("error", "Shipment not found");
                return new HttpResponse(HttpStatus.NOT_FOUND, arguments);
            }
        } else {
            ArrayList<HashMap<String, String>> result = DatabaseManager.selectRecords("Shipments", columns, null);
            return new HttpResponse(HttpStatus.OK, result);
        }
    }

    public HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        String orderId = parameters.get("order_id");
        String shipmentDate = parameters.get("shipment_date");
        String status = parameters.get("status");
        String trackingNumber = parameters.get("tracking_number");
        String[] columns = {"order_id", "shipment_date", "status", "tracking_number"};
        String[] values = {orderId, shipmentDate, status, trackingNumber};
        if (DatabaseManager.insertRecord("Shipments", columns, values)) {
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
        String[] columns = {"order_id", "shipment_date", "status", "tracking_number"};
        String[] values = {orderId, shipmentDate, status, trackingNumber};
        if (DatabaseManager.updateRecord("Shipments", columns, values, "id = '" + id + "'")) {
            return new HttpResponse(HttpStatus.OK, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error updating shipment");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }

    public HttpResponse handleDeleteRequest(Map<String, String> parameters, OutputStream outputStream) {
        String id = parameters.get("id");
        if (DatabaseManager.deleteRecord("Shipments", "id = '" + id + "'")) {
            return new HttpResponse(HttpStatus.OK, new HashMap<>());
        } else {
            HashMap<String, String> arguments = new HashMap<>();
            arguments.put("error", "Error deleting shipment");
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, arguments);
        }
    }
}
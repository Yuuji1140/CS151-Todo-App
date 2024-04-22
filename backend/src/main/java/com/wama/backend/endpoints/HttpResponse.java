package com.wama.backend.endpoints;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class HttpResponse {
    private final HttpStatus status;
    private final Map<String, String> arguments;

    public HttpResponse(HttpStatus status) {
        this.status = status;
        this.arguments = new HashMap<>();
    }

    public HttpResponse(HttpStatus status, Map<String, String> arguments) {
        this.status = status;
        this.arguments = arguments;
    }

    // Another constructor to take ArrayList instead of a map (in case we're returning many records)
    public HttpResponse(HttpStatus status, ArrayList<HashMap<String, String>> arguments) {
        this.status = status;
        this.arguments = new HashMap<>();
        for (int i = 0; i < arguments.size(); i++) {
            this.arguments.put(String.valueOf(i), arguments.get(i).toString());
        }
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return arguments.toString();
    }
}
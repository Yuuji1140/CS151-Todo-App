package com.wama.backend.endpoints;

import com.wama.backend.HttpStatus;

import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

public interface Endpoint {
    default HttpResponse handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("error", "GET request not implemented");
        return new HttpResponse(HttpStatus.NOT_IMPLEMENTED, arguments);
    }

    default HttpResponse handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("error", "POST request not implemented");
        return new HttpResponse(HttpStatus.NOT_IMPLEMENTED, arguments);
    }

    boolean validateParameters(Map<String, String> parameters);

}
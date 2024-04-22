package com.wama.backend.endpoints;

import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

public interface Endpoint {
    // Defaults allow for not implemented responses and further scaling for additional request types
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

    /*
    This method is used to validate the parameters of a request before handling it. The Server class
    calls this method before calling handleGetRequest or handlePostRequest.
     */
    boolean validateParameters(Map<String, String> parameters);

}
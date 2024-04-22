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

    default HttpResponse handlePutRequest(Map<String, String> parameters, OutputStream outputStream) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("error", "PUT request not implemented");
        return new HttpResponse(HttpStatus.NOT_IMPLEMENTED, arguments);
    }

    default HttpResponse handleDeleteRequest(Map<String, String> parameters, OutputStream outputStream) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("error", "DELETE request not implemented");
        return new HttpResponse(HttpStatus.NOT_IMPLEMENTED, arguments);
    }

    /*
    This method is used to validate the parameters of a request before handling it. The ConnectionHandler class
    calls this method before calling handleXXXRequest.
    Each individual endpoint should implement this method to validate the parameters of the request BUT SHOULD NOT
    CALL THE validateParameters METHOD DIRECTLY (that's the ConnectionHandler's job).
     */
    boolean validParameters(Map<String, String> parameters);

}
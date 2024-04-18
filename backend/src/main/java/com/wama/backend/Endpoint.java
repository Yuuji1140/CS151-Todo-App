package com.wama.backend;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface Endpoint {
    default HttpStatus handleGetRequest(Map<String, String> parameters, OutputStream outputStream) throws IOException {
        return HttpStatus.NOT_IMPLEMENTED;
    }

    default HttpStatus handlePostRequest(Map<String, String> parameters, OutputStream outputStream) throws IOException {
        return HttpStatus.NOT_IMPLEMENTED;
    }

    boolean validateParameters(Map<String, String> parameters);

}
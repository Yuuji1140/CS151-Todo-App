package com.wama.backend.endpoints;

import com.wama.backend.HttpStatus;

import java.io.OutputStream;
import java.util.Map;

public interface Endpoint {
    default HttpStatus handleGetRequest(Map<String, String> parameters, OutputStream outputStream) {
        return HttpStatus.NOT_IMPLEMENTED;
    }

    default HttpStatus handlePostRequest(Map<String, String> parameters, OutputStream outputStream) {
        return HttpStatus.NOT_IMPLEMENTED;
    }

    boolean validateParameters(Map<String, String> parameters);

}
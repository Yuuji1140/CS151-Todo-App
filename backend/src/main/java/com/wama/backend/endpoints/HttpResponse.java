package com.wama.backend.endpoints;

import java.util.Map;

public class HttpResponse {
    private final HttpStatus status;
    private final Map<String, String> arguments;

    public HttpResponse(HttpStatus status, Map<String, String> arguments) {
        this.status = status;
        this.arguments = arguments;
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
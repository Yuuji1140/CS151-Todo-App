package com.wama.backend;

public enum HttpStatus {
    // Massive Enum to handle HTTP status codes and such.
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    CONFLICT(409, "Conflict"),

    NOT_IMPLEMENTED(501, "Not Implemented"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    UNAUTHORIZED(401, "Unauthorized");

    private final int code;
    private final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
package com.wama.backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wama.backend.endpoints.Endpoint;
import com.wama.backend.endpoints.HttpResponse;
import com.wama.backend.endpoints.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

public class ConnectionHandler extends com.wama.LogClass implements Runnable {
    private final Socket socket;
    private final Map<String, Endpoint> endpoints;

    public ConnectionHandler(Socket socket, Map<String, Endpoint> endpoints) {
        this.socket = socket;
        this.endpoints = endpoints;
    }

    @Override
    public void run() {
        // https://www.javatpoint.com/java-socket-getinputstream-method
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream outputStream = socket.getOutputStream()) {
            info("Connection established with " + socket.getInetAddress().getHostAddress());

            String request = reader.readLine();
            if (request == null) {
                sendResponse(outputStream, HttpStatus.BAD_REQUEST, "Invalid request\\n");
                return;
            }

            String[] requestParts = request.split(" ");
            if (requestParts.length < 2) {
                sendResponse(outputStream, HttpStatus.BAD_REQUEST, "Invalid request\\n");
                return;
            }

            METHODS method = parseMethod(requestParts[0]);
            String endpointWithParams = requestParts[1];

            String[] endpointAndParams = extractEndpointAndParameters(endpointWithParams);
            String endpoint = endpointAndParams[0];
            String parametersString = endpointAndParams[1];

            Endpoint endpointHandler = endpoints.get(endpoint);

            if (endpointHandler == null) {
                sendResponse(outputStream, HttpStatus.NOT_FOUND, "Endpoint not found\\n");
                return;
            }

            Map<String, String> parameters = new HashMap<>();
            Map<String, String> extractedParameters = extractParameters(parametersString);
            if (extractedParameters != null)
                parameters.putAll(extractedParameters);
            Map<String, String> extractedJsonParameters = extractJsonParameters(readRequestBody(reader));
            if (extractedJsonParameters != null)
                parameters.putAll(extractedJsonParameters);

            if (!endpointHandler.validParameters(parameters)) {
                sendResponse(outputStream, HttpStatus.BAD_REQUEST, "Invalid parameters\\n");
                return;
            }

            HttpResponse status = handleRequest(method, endpointHandler, parameters, outputStream);
            sendResponse(outputStream, status.getStatus(), status + "\\n");
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg.contains("Unsupported or unrecognized SSL message"))
                errorMsg = "Unsupported or unrecognized SSL message. Did you forget to use HTTPS?";
            error("Error while handling connection: " + errorMsg);
            debug("Error details: \n" + e + "\n" + Arrays.toString(e.getStackTrace()) + "\n");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                error("Error while closing socket: " + e.getMessage(), e);
            }
        }
    }

    private static METHODS parseMethod(String methodString) {
        try {
            return METHODS.valueOf(methodString);
        } catch (IllegalArgumentException e) {
            return METHODS.INVALID;
        }
    }

    private static Map<String, String> extractJsonParameters(String requestBody) {
        Gson gson = new Gson();
        return gson.fromJson(requestBody, new TypeToken<Map<String, String>>() {}.getType());
    }

    private static String[] extractEndpointAndParameters(String endpointWithParams) {
        // For GET requests
        String[] parts = endpointWithParams.split("\\?", 2);
        String endpoint = parts[0];
        String params = (parts.length > 1) ? parts[1] : "";
        return new String[]{endpoint, params};
    }

    private static Map<String, String> extractParameters(String parametersString) {
        //  For GET requests
        Map<String, String> parameters = new HashMap<>();
        parameters.put("requestType", "GET");
        String[] pairs = parametersString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                parameters.put(keyValue[0], keyValue[1]);
            }
        }
        return parameters;
    }

    private HttpResponse handleRequest(METHODS method, Endpoint endpointHandler, Map<String, String> parameters, OutputStream outputStream) {
        switch (method) {
            case GET:
                parameters.put("requestType", "GET");
                return endpointHandler.handleGetRequest(parameters, outputStream);
            case POST:
                parameters.put("requestType", "POST");
                return endpointHandler.handlePostRequest(parameters, outputStream);
            case PUT:
                parameters.put("requestType", "PUT");
                return endpointHandler.handlePutRequest(parameters, outputStream);
            case DELETE:
                parameters.put("requestType", "DELETE");
                return endpointHandler.handleDeleteRequest(parameters, outputStream);
            default:
                return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private static String readRequestBody(BufferedReader reader) throws IOException {
        /*
        This method reads the request body after discarding the headers.
         */
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            assert true; // Do nothing, just read until we're past the headers.
        }

        StringBuilder requestBody = new StringBuilder();
        while (reader.ready()) {
            // These are all the parameter strings.
            requestBody.append((char) reader.read());
        }
        return requestBody.toString();
    }

    private static void sendResponse(OutputStream outputStream, HttpStatus status, String content) throws IOException {
        String response = "HTTP/1.1 " + status.getCode() + " " + status.getMessage() + "\r\n"
                + "Content-Type: text/plain\r\n"
                + "Content-Length: " + content.length() + "\r\n"
                + "\r\n"
                + content;
        outputStream.write(response.getBytes());
        outputStream.flush();
    }

    private enum METHODS {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE"),
        INVALID("INVALID");

        METHODS(String method) {
            assert this.name().equals(method); // Do something to keep IDE happy
        }
    }
}

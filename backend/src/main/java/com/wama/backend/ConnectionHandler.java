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

            if (request != null) {
                String[] requestParts = request.split(" ");
                METHODS method = parseMethod(requestParts[0]);
                String endpoint = requestParts[1];

                Endpoint endpointHandler = endpoints.get(endpoint);
                if (endpointHandler != null) {
                    String requestBody = readRequestBody(reader);
                    Map<String, String> parameters = extractJsonParameters(requestBody);

                    // Validate parameters before handling request (Interface allows for Endpoint validation)
                    if (endpointHandler.validateParameters(parameters)) {
                        HttpResponse status = handleRequest(method, endpointHandler, parameters, outputStream);
                        sendResponse(outputStream, status.getStatus(), status + "\n");
                    } else {
                        sendResponse(outputStream, HttpStatus.BAD_REQUEST, "Invalid parameters\n");
                    }
                } else {
                    sendResponse(outputStream, HttpStatus.NOT_FOUND, "Endpoint not found\n");
                }
            } else {
                sendResponse(outputStream, HttpStatus.BAD_REQUEST, "Invalid request\n");
            }
        } catch (Exception e) {
            error("Error while handling connection: " + e.getMessage(), e);
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

    private HttpResponse handleRequest(METHODS method, Endpoint endpointHandler, Map<String, String> parameters, OutputStream outputStream) {
        switch (method) {
            case GET:
                return endpointHandler.handleGetRequest(parameters, outputStream);
            case POST:
                return endpointHandler.handlePostRequest(parameters, outputStream);
            default:
                return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, null);
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
        INVALID("INVALID");

        METHODS(String method) {
            assert this.name().equals(method); // Do something to keep IDE happy
        }
    }
}

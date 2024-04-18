package com.wama.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

public class Server {
//    private static final int PORT = 9876;
//    private static final String KEYSTORE_FILE = "keystore.jks";
//    private static final String KEYSTORE_PASSWORD = "password";
//    private static final String METHOD_GET = "GET";
//    private static final String METHOD_POST = "POST";
    private static final int PORT;
    private static final String KEYSTORE_FILE;
    private static final String KEYSTORE_PASSWORD;
    private static final Map<String, Endpoint> endpoints = new HashMap<>();

    static {
        // Register endpoints
        PORT = 9876;
        KEYSTORE_FILE = "keystore.jks";
        KEYSTORE_PASSWORD = "password";
        endpoints.put("/authUser", new AuthUser());
        endpoints.put("/registerUser", new RegisterUser());
        DatabaseManager.createDatabase();
    }

    public static void main(String[] args) {
        System.out.println("Server started.");


        try (ServerSocket serverSocket = createSSLServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     OutputStream outputStream = socket.getOutputStream()) {
                    System.out.println("Client connected.");
                    String request = reader.readLine();

                    if (request != null) {
                        String[] requestParts = request.split(" ");
                        METHODS method = parseMethod(request.split(" ")[0]);
                        String endpoint = requestParts[1];

                        Map<String, String> parameters;
                        Endpoint endpointHandler = endpoints.get(endpoint);

                        if (endpointHandler != null) {
                            switch (method) {
                                case GET:
                                    String queryString = endpoint;
                                    if (queryString.contains("?")) {
                                        queryString = queryString.split("\\?")[1];
                                    }
                                    parameters = extractParameters(queryString);
                                    if (endpointHandler.validateParameters(parameters)) {
                                        HttpStatus status = endpointHandler.handleGetRequest(parameters, outputStream);
                                        sendResponse(outputStream, status, status.getMessage() + "\n");
                                    } else {
                                        sendResponse(outputStream, HttpStatus.BAD_REQUEST, "Invalid parameters\n");
                                    }
                                    break;
                                case POST:
                                    String requestBody = readRequestBody(reader);
                                    parameters = extractParameters(requestBody);
                                    if (endpointHandler.validateParameters(parameters)) {
                                        HttpStatus status = endpointHandler.handlePostRequest(parameters, outputStream);
                                        sendResponse(outputStream, status, status.getMessage() + "\n");
                                    } else {
                                        sendResponse(outputStream, HttpStatus.BAD_REQUEST, "Invalid parameters\n");
                                    }
                                    break;
                                default:
                                    sendResponse(outputStream, HttpStatus.METHOD_NOT_ALLOWED, "Invalid HTTP method\n");
                                    break;
                            }
                        } else {
                            sendResponse(outputStream, HttpStatus.NOT_FOUND, "Endpoint not found\n");
                        }
                    } else {
                        sendResponse(outputStream, HttpStatus.BAD_REQUEST, "Invalid request\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static METHODS parseMethod(String methodString) {
        try {
            return METHODS.valueOf(methodString);
        } catch (IllegalArgumentException e) {
            return METHODS.INVALID;
        }
    }
    private static Map<String, String> extractParameters(String parameterString) {
        Map<String, String> parameters = new HashMap<>();
        // We need to trim the string down to the first non-whitespace character BEFORE the first & symbol

        String[] parameterPairs = parameterString.split("&");
        for (String pair : parameterPairs) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                parameters.put(key, value);
            }
        }
        return parameters;
    }

    private static String readRequestBody(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            assert true; // Do nothing, just read until we're past the headers.
        }

        StringBuilder requestBody = new StringBuilder();
        while (reader.ready()) {
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

    private static SSLServerSocket createSSLServerSocket(int port) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(KEYSTORE_FILE), KEYSTORE_PASSWORD.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();

        SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);

        return serverSocket;
    }
}

enum METHODS {
    GET("GET"),
    POST("POST"),
    INVALID("INVALID");

    private final String method;

    METHODS(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}

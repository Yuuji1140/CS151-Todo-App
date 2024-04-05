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

/*
Examples:
C:\Users\trevor\code\CS151-WaMa>curl -k -X POST --data-binary "username=johndoe&email=john@example.com&p
assword=secret123" -H "Content-Type: application/x-www-form-urlencoded" https://localhost:9876
POST request received
C:\Users\trevor\code\CS151-WaMa>curl -k "https://localhost:9876/users?param1=value1&param2=value2"
GET request received

Output:
Server started.
Server listening on port 9876
Client connected.
POST request parameters:
password: secret123
Host: localhost:9876
User-Agent: curl/8.4.0
Accept: *//*
Content-Type: application/x-www-form-urlencoded
Content-Length: 58

username: johndoe
email: john@example.com
Client connected.
POST request parameters:
param1: value1
param2: value2
 */

public class Server {
    private static final int PORT = 9876;
    private static final String KEYSTORE_FILE = "keystore.jks"; // For SSL
    // TODO: Replace with private/public keys

    private static final String KEYSTORE_PASSWORD = "password";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    public static void main(String[] args) {
        System.out.println("Server started.");

        try (ServerSocket serverSocket = createSSLServerSocket(PORT)) {
            // Server has started
            System.out.println("Server listening on port " + PORT);

            while (true) {
                // Continuously listen for incoming connections
                // TODO: Implement a thread pool
                try (Socket socket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     OutputStream outputStream = socket.getOutputStream()) {
                    System.out.println("Client connected.");
                    String request = reader.readLine();

                    if (request != null) {
                        String method = request.split(" ")[0];
                        Map<String, String> parameters; // Initialized in switch by returned object

                        switch (method) {
                            // TODO: Add support for PUT, DELETE and various /endpoints
                            // TODO: Convert to JSON and use a library for parsing
                            // TODO: Be more object oriented and have a Request class or something that can
                            //       handle the request and return a response across various endpoints and methods
                            case METHOD_GET:
                                // Catch exception if [1] isn't there
                                String queryString = request.split(" ")[1];
                                if (queryString.contains("?")) {
                                    queryString = queryString.split("\\?")[1];
                                }
                                parameters = extractParameters(queryString);
                                handleGetRequest(parameters, outputStream);
                                break;
                            case METHOD_POST:
                                String requestBody = readRequestBody(reader);
                                parameters = extractParameters(requestBody);
                                handlePostRequest(parameters, outputStream);
                                break;
                            default:
                                sendResponse(outputStream, HttpStatus.METHOD_NOT_ALLOWED, "Invalid HTTP method");
                                break;
                        }
                    } else {
                        sendResponse(outputStream, HttpStatus.BAD_REQUEST, "Invalid request");
                    }
                } catch (Exception e) {
                    // TODO: Create better error handling in a utility class, print stacktrace and log stuff
                    // TODO: Replace all printStackTrace with logging from above
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> extractParameters(String parameterString) {
        Map<String, String> parameters = new HashMap<>();
        String[] parameterPairs = parameterString.split("&");
        for (String pair : parameterPairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                // Strip leading /
                if (parts[0].startsWith("/")) {
                    parts[0] = parts[0].substring(1);
                }
                parameters.put(parts[0], parts[1]);
            }
        }
        return parameters;
    }

    private static String readRequestBody(BufferedReader reader) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        while (reader.ready()) {
            requestBody.append((char) reader.read());
        }
        return requestBody.toString();
    }

    private static void handleGetRequest(Map<String, String> parameters, OutputStream outputStream) throws IOException {
        // Print every parameter received in the POST request
        System.out.println("POST request parameters:");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        sendResponse(outputStream, HttpStatus.OK, "GET request received");
    }

    private static void handlePostRequest(Map<String, String> parameters, OutputStream outputStream) throws IOException {
        // Print every parameter received in the POST request
        System.out.println("POST request parameters:");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        sendResponse(outputStream, HttpStatus.OK, "POST request received");
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

        // Create a server socket with SSL/TLS support
        // Needs to be redundant in case we need to enable mutliple TLS protocols again
        SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);

        // Enable multiple SSL/TLS protocol versions
        //serverSocket.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.1", "TLSv1"});

        return serverSocket;
    }

    private enum HttpStatus {
        // Massive Enum to handle HTTP status codes and such.
        // TODO: This should probably be moved to its own class
        OK(200, "OK"),
        BAD_REQUEST(400, "Bad Request"),
        METHOD_NOT_ALLOWED(405, "Method Not Allowed");

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
}
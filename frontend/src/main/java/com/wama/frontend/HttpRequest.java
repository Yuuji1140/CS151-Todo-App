package com.wama.frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class HttpRequest {
    private static final String BASE_URL = "trevors-laptop";
    private static final int PORT = 9876;

    private static String urlString(String endpoint) {
        return "https://" + BASE_URL + ":" + PORT + endpoint;
    }

    // Frontend calls this method
    private static String sendRequest(String endpoint, String requestMethod, Map<String, String> parameters)
            throws IOException {
        HttpURLConnection connection = null;

        if (requestMethod.equals("POST") || requestMethod.equals("PUT")) {
            URL url = new URL(urlString(endpoint));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonBody = new Gson().toJson(parameters);

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }
        }

        if (requestMethod.equals("GET")) {
            StringBuilder queryParams = new StringBuilder();
            for (Map.Entry<String, String> param : parameters.entrySet()) {
                if (!queryParams.isEmpty()) {
                    queryParams.append("&");
                }
                queryParams.append(param.getKey()).append("=").append(param.getValue());
            }
            URL url = new URL(urlString(endpoint) + "?" + queryParams);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
        }

        int responseCode = connection != null ? connection.getResponseCode() : 0;

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                String responsePayload = response.toString();
                // Process the response payload as needed
                System.out.println("Response Payload: " + responsePayload);
                return responsePayload;
            }
        } else {
            // Handle error response
            try (InputStream errorStream = connection.getErrorStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream))) {
                String line;
                StringBuilder errorResponse = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    errorResponse.append(line);
                }
                String errorPayload = errorResponse.toString();
                // Process the error payload as needed
                System.out.println("Error Payload: " + errorPayload);
                return "Error registering user: " + errorPayload;
            }
        }
    }

    public static String get(String endpoint, HashMap<String, String> parameters) throws IOException {
        return sendRequest(endpoint, "GET", parameters);
    }

    public static String post(String endpoint, Map<String, String> parameters) throws IOException {
        return sendRequest(endpoint, "POST", parameters);
    }

    public static String put(String endpoint, Map<String, String> parameters) throws IOException {
        return sendRequest(endpoint, "PUT", parameters);
    }

    public static String delete(String endpoint) throws IOException {
        return sendRequest(endpoint, "DELETE", null);
    }
}
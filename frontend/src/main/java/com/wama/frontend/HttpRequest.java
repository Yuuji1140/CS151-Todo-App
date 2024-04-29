package com.wama.frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.google.gson.Gson;

public class HttpRequest {

    // Frontend calls this method
    public static String post(String urlString, Map<String, String> parameters) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        String jsonBody = new Gson().toJson(parameters);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        // TODO: Handle response code for all endpoints (see backend/endpoints/ConnectionHandler.java METHODS enum)

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
        } 
        else {
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
}
package com.wama.frontend;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
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
            // Handle successful response
            return "User registered successfully";
        } else {
            // Handle error response
            return "Error registering user";
        }
    }
}

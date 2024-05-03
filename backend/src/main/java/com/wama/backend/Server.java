package com.wama.backend;

import com.wama.backend.endpoints.*;
import com.wama.DatabaseManager;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server extends com.wama.LogClass {
    private static final int PORT;
    private static final Map<String, Endpoint> endpoints;

    static {
        // Register endpoints
        PORT = 9876;
        endpoints = new HashMap<>();
        endpoints.put("/authUser", new AuthUser());
        endpoints.put("/registerUser", new RegisterUser());
        endpoints.put("/products", new Products());
        endpoints.put("/orders", new Orders());
        endpoints.put("/shipments", new Shipments());
        DatabaseManager.createDatabase();
    }

    public static void main(String[] args) {
        /*
         The main loop of the server. It listens for incoming connections and creates a new ConnectionHandler
         for each connection in a new thread. Each ConnectionHandler reads the request, extracts the endpoint and
         parameters, validates parameters, calls the appropriate Endpoint, and responds to the user.
         */
        info("Starting server on port " + PORT + "...");
        // TODO: Will putting this in an out while true: loop allow for the server to restart on failure?
        //       would need to look into retry counts and reasons to fail.
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            info("Server started successfully on port " + PORT + ".");

            // We can handle requests now.
            while (serverSocket.isBound()) {
                Socket socket = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(socket, endpoints);
                new Thread(connectionHandler).start();
                // There is no need to join the thread
            }
            // TODO: Ensure we don't need a shutdown type hook to close the server on failure
            //       I think isBound() is enough.
        } catch (Exception e) {
            error("An error occurred while starting the server", e);
        }
    }
}


package com.wama.backend;

import com.wama.backend.endpoints.AuthUser;
import com.wama.backend.endpoints.Endpoint;
import com.wama.backend.endpoints.RegisterUser;
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
        DatabaseManager.createDatabase();
    }

    public static void main(String[] args) {
        info("Starting server on port " + PORT + "...");


        try (ServerSocket serverSocket = SSLFactory.getInstance().createSSLServerSocket(PORT)) {
            info("Server started successfully on port " + PORT + ".");

            while (serverSocket.isBound()) {
                Socket socket = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(socket, endpoints);
                new Thread(connectionHandler).start();
            }
        } catch (Exception e) {
            error("An error occurred while starting the server", e);
        }
    }
}


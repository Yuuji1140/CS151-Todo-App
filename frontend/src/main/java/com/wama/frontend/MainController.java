package com.wama.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {
    @FXML
    private Button signUpButton;

    @FXML
    private Button loginButton;

    @FXML
    private void handleSignUp() {
        System.out.println("Go to sign-up screen!");
        // add sign-up logic here
    }

    @FXML
    private void handleLogin() {
        System.out.println("Go to login screen!");
        // add login logic here
    }
}
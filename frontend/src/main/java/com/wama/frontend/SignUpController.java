package com.wama.frontend;

import java.io.IOException;
import java.util.HashMap;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SignUpController {
    private Stage stage;
    private Scene scene;

    @FXML private Pane animationPane;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField rePasswordField;
    @FXML private ComboBox<String> userTypeComboBox;
    
    @FXML
    private Text usernameError, emailError, passwordError, rePasswordError;

    @FXML
    public void initialize() {
        userTypeComboBox.getItems().addAll("Customer", "Employee");
        userTypeComboBox.setValue("Customer");
        
        AnimSlidingRects.generateRectangles(animationPane, 3, -525, 35, 0);
        AnimSlidingRects.generateRectangles(animationPane, 3, -525, 185, 0);
        AnimSlidingRects.generateRectangles(animationPane, 3, -525, 335, 0);
        AnimSlidingRects.generateRectangles(animationPane, 3, -525, 485, 0);
        
        fadeInEffect(animationPane, 3500);
    }

    @FXML
    void handleBackButtonAction(ActionEvent event) {
        Main.switchToSceneStartUp();
    }

    public void handleSignUp(ActionEvent event) {
        boolean isValid = true;

        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String rePassword = rePasswordField.getText();
        String userType = userTypeComboBox.getValue();
        String nameOfUser = "Demo User";
        String phone = "1234567890";
        String address = "1234 Demo User Way. Demo City, Demo State, 12345";
        String companyName = userType.equalsIgnoreCase("Customer") ? "Demo Company" : "";

        String usernameErrorText = validateUsername(username);
        if (!usernameErrorText.isEmpty()) {
            usernameError.setText(usernameErrorText);
            usernameError.setVisible(true);
            isValid = false;
        } 
        else
            usernameError.setVisible(false);

        if (!validateEmail(email)) {
            emailError.setText("Invalid Email!");
            emailError.setVisible(true);
            isValid = false;
        }
        else
            emailError.setVisible(false);

        if (!validatePassword(password)) {
            passwordError.setText("Password must be at least 8 characters!");
            passwordError.setVisible(true);
            isValid = false;
        }
        else
            passwordError.setVisible(false);

        if (!password.equals(rePassword)) {
            rePasswordError.setText("Passwords do not match!");
            rePasswordError.setVisible(true);
            isValid = false;
        }
        else
            rePasswordError.setVisible(false);

        if (isValid) {
            System.out.println("Field validation succeeded!");
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("username", username);
            parameters.put("email", email);
            parameters.put("password", password);
            parameters.put("type", userType);
            parameters.put("company_name", companyName);
            parameters.put("name", nameOfUser);
            parameters.put("phone", phone);
            parameters.put("address", address);

            try {
                String response = HttpRequest.post("http://localhost:9876/registerUser", parameters);
                System.out.println(response);
                Main.switchToSceneLogin();
                // Handle response, e.g., show success or error message
            } 
            catch (IOException e) {
                // Handle exception
            }
        }
        else
            System.out.println("Field validation failed!");
    }

    private String validateUsername(String username) {
        if (username.length() < 3)
            return "Username must be at least 3 characters!";
        if (!username.matches("[a-zA-Z0-9]+"))
            return "Username can only contain letters and numbers!";
        return ""; // no error
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8;
    }

    private boolean validateEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    private void fadeInEffect(Node node, double duration) {
        FadeTransition ft = new FadeTransition(Duration.millis(duration), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    public void scene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/wama/frontend/scenes/SceneSignUp.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
package com.wama.frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController
{
	private Stage stage;
	private Scene scene;
	
	@FXML private Pane animationPane;
	
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	
	@FXML
    public void initialize() {        
        AnimSlidingRects.generateRectangles(animationPane, 3, -675, 35, 0);
        AnimSlidingRects.generateRectangles(animationPane, 3, -675, 185, 0);
        AnimSlidingRects.generateRectangles(animationPane, 3, -675, 335, 0);
        AnimSlidingRects.generateRectangles(animationPane, 3, -675, 485, 0);
        
        AnimSlidingRects.generateRectangles(animationPane, 3, 1000, 35, 0);
        AnimSlidingRects.generateRectangles(animationPane, 3, 1000, 185, -100);
        AnimSlidingRects.generateRectangles(animationPane, 3, 1000, 335, -100);
        AnimSlidingRects.generateRectangles(animationPane, 3, 1000, 485, -100);
        
        fadeInEffect(animationPane, 3500);
    }
	
	@FXML
    void handleBackButtonAction(ActionEvent event) {
        Main.switchToSceneStartUp();
    }
	
	void switchToDashboard(Map<String, String> userData) {
	    String userType = userData.get("type");
	    if ("Customer".equalsIgnoreCase(userType))
	        Main.switchToSceneDashboardCustomer();
	    else if ("Employee".equalsIgnoreCase(userType))
	        Main.switchToSceneDashboardEmployee();
	    else 
	        System.err.println("Unknown user type: " + userType);
	}
	
	@FXML
	public void handleLogin(ActionEvent event) {
	    String username = usernameField.getText().trim();
	    String password = passwordField.getText().trim();
	    String userType = "Customer"; // hard coded in at the moment, unsure of how to dynamically retrive usertype when logging in, didn't want to modify backend -hike

	    if (username.isEmpty() || password.isEmpty()) {
	        showAlert("Error", "Username and password cannot be empty!");
	        return;
	    }

	    try {
	        String response = sendLoginRequest(username, password, userType); // originally sent with a combobox, but during login the program should automatically check the usertype
	        if (response.contains("authToken=")) {
	            Map<String, String> userData = parseUserData(response);
	            System.out.println("Successfully logged in! Auth Token: " + userData.get("authToken"));
	            switchToDashboard(userData);
	        } 
	        else
	            showAlert("Login Failed", "Invalid credentials or server error!");
	    } 
	    catch (IOException e) {
	        showAlert("Network Error", "Failed to connect to server!");
	        System.out.println("Network error: " + e.getMessage());
	    }
	}

	private String sendLoginRequest(String username, String password, String userType) throws IOException {
	    Map<String, String> parameters = new HashMap<>();
	    parameters.put("username", username);
	    parameters.put("password", password);
	    parameters.put("type", userType);
	    return HttpRequest.post("http://localhost:9876/authUser", parameters);
	}
	
	private Map<String, String> parseUserData(String response) {
	    Map<String, String> userData = new HashMap<>();
	    userData.put("username", extractValue(response, "username"));
	    userData.put("email", extractValue(response, "email"));
	    userData.put("id", extractValue(response, "id"));
	    userData.put("authToken", extractValue(response, "authToken"));
	    userData.put("type", extractValue(response, "type"));
	    return userData;
	}

    private String extractValue(String response, String key) {
        String prefix = key + "=";
        int startIndex = response.indexOf(prefix) + prefix.length();
        int endIndex = response.indexOf(",", startIndex);
        endIndex = endIndex == -1 ? response.length() : endIndex;
        return response.substring(startIndex, endIndex).trim();
    }

	private void showAlert(String title, String content) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(content);
	    alert.showAndWait();
	}
	
	private void fadeInEffect(Node node, double duration) {
	    FadeTransition ft = new FadeTransition(Duration.millis(duration), node);
	    ft.setFromValue(0.0);
	    ft.setToValue(1.0);
	    ft.play();
	}
	
	public void scene(ActionEvent event) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("/com/wama/frontend/scenes/SceneLogin.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
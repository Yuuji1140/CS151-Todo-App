package com.wama.frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	    if (username.isEmpty() || password.isEmpty()) {
	        showAlert("Error", "Username and password cannot be empty!");
	        return;
	    }

	    try {
	        String response = sendLoginRequest(username, password);

			HashMap<String, String> userData = parseUserData(response);
			LoggedInUser user = LoggedInUser.getInstance(); // This creates a singleton logged in user
			user.setUser(userData.get("id"), username, userData.get("email"), userData.get("company_name"),
					userData.get("company_id") ,userData.get("name"), userData.get("phone"), userData.get("address"));
			System.out.println("User ID: " + user.getUserId());
			System.out.println("Company id: " + user.getCompanyName());
			if (userData.containsKey("id")) {
				System.out.println("Successfully logged in!");
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

	private String sendLoginRequest(String username, String password) throws IOException {
	    Map<String, String> parameters = new HashMap<>();
	    parameters.put("username", username);
	    parameters.put("password", password);
	    return HttpRequest.post("http://localhost:9876/authUser", parameters);
	}
	
	private HashMap<String, String> parseUserData(String response) {
		HashMap<String, String> userData = new HashMap<>();

		// regular expression pattern to match key-value pairs
		String pattern = "(\\w+)=([^,}]+)";

		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(response);

		while (matcher.find()) {
			String key = matcher.group(1);
			String value = matcher.group(2);
			userData.put(key, value);
		}

		return userData;
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
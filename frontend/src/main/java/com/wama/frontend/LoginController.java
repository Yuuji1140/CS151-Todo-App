package com.wama.frontend;

import java.io.IOException;

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
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController
{
	private Stage stage;
	private Scene scene;
	
	@FXML private Pane animationPane;
	
	@FXML private ComboBox<String> userTypeComboBox;
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	
	@FXML
    public void initialize() {
		userTypeComboBox.getItems().addAll("Customer", "Employee");
        userTypeComboBox.setValue("Customer");
        
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
	
	public void handleLogin(ActionEvent event) {
		
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
package com.wama.frontend;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.HashMap;

@SuppressWarnings("unused")
public class StartUpController
{
	private Stage stage;
	private Scene scene;
	
	@FXML private Pane animationPane;

    @FXML
    public void initialize() {
        AnimSlidingRects.generateRectangles(animationPane, 3, -250, 25, 0);
        AnimSlidingRects.generateRectangles(animationPane, 3, 600, 625, -100);
        fadeInEffect(animationPane, 3500);
    }
	
	public void scene(ActionEvent event) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("/com/wama/frontend/scenes/SceneStartUp.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
    void handleSignUpButtonAction(ActionEvent event) {
        Main.switchToSceneSignUp();
    }
	
	@FXML
    void handleLoginButtonAction(ActionEvent event) {
        Main.switchToSceneLogin();
    }
	
	private void fadeInEffect(Node node, double duration) {
	    FadeTransition ft = new FadeTransition(Duration.millis(duration), node);
	    ft.setFromValue(0.0);
	    ft.setToValue(1.0);
	    ft.play();
	}
}
package com.wama.frontend;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SignUpController
{
	private Stage stage;
	private Scene scene;
	
	@FXML private Pane animationPane;
	
	@FXML
    public void initialize() {
        ShapeFactory.generateRectangles(animationPane, 3, -250, 25, 0);
        ShapeFactory.generateRectangles(animationPane, 3, 600, 625, -100);
        fadeInEffect(animationPane, 3500);
    }
	
	public void scene(ActionEvent event) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("/com/wama/frontend/scenes/SceneSignUp.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
    void handleBackButtonAction(ActionEvent event) {
        Main.switchToSceneStartUp();
    }
	
	private void fadeInEffect(Node node, double duration) {
	    FadeTransition ft = new FadeTransition(Duration.millis(duration), node);
	    ft.setFromValue(0.0);
	    ft.setToValue(1.0);
	    ft.play();
	}
}
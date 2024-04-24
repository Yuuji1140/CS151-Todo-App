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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

@SuppressWarnings("unused")
public class SceneController
{
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML private Pane animationPane;
	@FXML private Text text1;
	@FXML private Button signUpButton;

    @FXML
    public void initialize() {
        ShapeFactory.generateRectangles(animationPane, 3, -250, 25, 0);
        ShapeFactory.generateRectangles(animationPane, 3, 600, 625, -100);
        fadeInEffect(animationPane, 3500);
    }
	
	public void sceneMain(ActionEvent event) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("SceneStartUp.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void sceneSignUp(ActionEvent event) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("SceneSignUp.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void sceneLogin(ActionEvent event) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("SceneLogin.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	private void fadeInEffect(Node node, double durationMillis) {
	    FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), node);
	    ft.setFromValue(0.0);  // Start fully transparent
	    ft.setToValue(1.0);    // End fully opaque
	    ft.play();
	}

}
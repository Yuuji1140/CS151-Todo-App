package com.wama.frontend;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	private static Stage stage;
	
    @Override
    public void init() throws Exception {
        super.init();
    }
    
    @Override
    public void start(Stage stage) {
    	Main.stage = stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wama/frontend/scenes/SceneCustomerDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/wama/frontend/scenes/style.css").toExternalForm());
            stage.getIcons().add(new Image("/com/wama/frontend/images/icon.png"));
            stage.setResizable(false);
            stage.setTitle("WaMa Systems");
            stage.setScene(scene);
            stage.show();
        } 
        catch (Exception e) { e.printStackTrace(); }
    }
    
    public static void switchToSceneStartUp() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/wama/frontend/scenes/SceneStartUp.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } 
        catch (IOException e) { e.printStackTrace(); }
    }

    public static void switchToSceneSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/wama/frontend/scenes/SceneSignUp.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } 
        catch (IOException e) { e.printStackTrace(); }
    }
    
    public static void switchToSceneLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/wama/frontend/scenes/SceneLogin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } 
        catch (IOException e) { e.printStackTrace(); }
    }
    
    public static void switchToSceneFeedbackEmployee() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/wama/frontend/scenes/SceneFeedback.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } 
        catch (IOException e) { e.printStackTrace(); }
    }
    
    public static void switchToSceneFeedbackCustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/wama/frontend/scenes/SceneFeedback.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } 
        catch (IOException e) { e.printStackTrace(); }
    }
    
    public static void switchToSceneDashboardEmployee() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/wama/frontend/scenes/SceneEmployeeDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } 
        catch (IOException e) { e.printStackTrace(); }
    }
    
    public static void switchToSceneDashboardCustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/wama/frontend/scenes/SceneCustomerDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } 
        catch (IOException e) { e.printStackTrace(); }
    }
    
    public static void switchToSceneOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/wama/frontend/scenes/SceneOrders.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } 
        catch (IOException e) { e.printStackTrace(); }
    }
    
    public static void switchToSceneTracking() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/wama/frontend/scenes/SceneTracking.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } 
        catch (IOException e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
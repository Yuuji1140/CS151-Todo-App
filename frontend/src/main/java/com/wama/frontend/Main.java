package com.wama.frontend;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    @Override
    public void init() throws Exception {
        super.init();
        Font.loadFont(getClass().getResourceAsStream("/fonts/MONACO.ttf"), 14);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/main.fxml"));
        Parent fxmlRoot = loader.load();
        Pane root = new Pane();
        Scene scene = new Scene(root, 1024, 768);

        ShapeFactory.generateRectangles(root, 3, 0, 25, 2, true);
        ShapeFactory.generateRectangles(root, 3, -250, 625, 2, false);

        root.getChildren().add(fxmlRoot);

        // center fxmlRoot within the pane
        fxmlRoot.setLayoutX(((root.getWidth() - fxmlRoot.prefWidth(-1)) / 2) - 225);
        fxmlRoot.setLayoutY(((root.getHeight() - fxmlRoot.prefHeight(-1)) / 2) - 125);

        TextFlow titleTextFlow = (TextFlow) loader.getNamespace().get("titleTextFlow");
        Button signUpButton = (Button) loader.getNamespace().get("signUpButton");
        Button loginButton = (Button) loader.getNamespace().get("loginButton");

        // fadetransition for textflow & buttons
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), titleTextFlow);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        // apply same transition to buttons
        FadeTransition buttonFade = new FadeTransition(Duration.seconds(3), signUpButton);
        buttonFade.setFromValue(0);
        buttonFade.setToValue(1);

        FadeTransition loginButtonFade = new FadeTransition(Duration.seconds(3), loginButton);
        loginButtonFade.setFromValue(0);
        loginButtonFade.setToValue(1);

        // disable buttons initially
        signUpButton.setDisable(true);
        loginButton.setDisable(true);

        // enable buttons when fade-in finishes
        fadeTransition.setOnFinished(event -> {
            signUpButton.setDisable(false);
            loginButton.setDisable(false);
        });

        // play transitions
        fadeTransition.play();
        buttonFade.play();
        loginButtonFade.play();

        stage.setResizable(false);
        stage.getIcons().add(new Image("/app/images/icon.png"));
        stage.setTitle("Warehouse Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package com.wama.frontend.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wama.frontend.HttpRequest;
import com.wama.frontend.LoggedInUser;
import com.wama.frontend.Main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ShippingController
{
	LoggedInUser user = LoggedInUser.getInstance();
    HashMap<String, HashMap<String, String>> shipments;

    @FXML
    private ScrollPane mainContent;

    @FXML
    private TilePane tilePane;

    public void initialize() throws IOException {
        UpdaterThread.createNew(this::loadShipments, 5);
    }

    @FXML
    private void loadShipments() {
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("customer_id", user.getCompanyId());
            String response = HttpRequest.get("/shipments", parameters);
            this.shipments = parseShipmentData(response);
            displayShipments();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, HashMap<String, String>> parseShipmentData(String response) {
        HashMap<String, HashMap<String, String>> shipmentData = new HashMap<>();
        
        // regular expression pattern to match key-value pairs
        String outerPattern = "(\\d+)=(\\{[^}]*\\})";
        Pattern outerRegex = Pattern.compile(outerPattern);
        Matcher outerMatcher = outerRegex.matcher(response);

        while (outerMatcher.find()) {
            String shipmentId = outerMatcher.group(1);
            String details = outerMatcher.group(2);

            HashMap<String, String> detailsMap = new HashMap<>();
            String innerPattern = "(\\w+)=([^,}]+)";
            Pattern innerRegex = Pattern.compile(innerPattern);
            Matcher innerMatcher = innerRegex.matcher(details);

            while (innerMatcher.find()) {
                String key = innerMatcher.group(1);
                String value = innerMatcher.group(2).trim();
                detailsMap.put(key, value);
            }
            shipmentData.put(shipmentId, detailsMap);
        }

        return shipmentData;
    }

    private void displayShipments() {
        Platform.runLater(() -> {
            tilePane.getChildren().clear();
            for (HashMap.Entry<String, HashMap<String, String>> entry : shipments.entrySet()) {
                HashMap<String, String> shipment = entry.getValue();

                VBox shipmentBox = new VBox(10);
                shipmentBox.getStyleClass().add("shipment-box");
                shipmentBox.setPadding(new Insets(10));

                Text shipmentId = new Text("Shipment ID: " + shipment.get("id"));
                Text shipmentDate = new Text("Shipment Date: " + shipment.get("shipment_date"));
                Text shipmentStatus = new Text("Status: " + shipment.get("status"));
                Text trackingNumber = new Text("Tracking Number: " + shipment.get("tracking_number"));

                Button changeStatusButton = new Button("Change Status");
                changeStatusButton.setOnAction(e -> changeShipmentStatus(shipment.get("id")));

                shipmentBox.getChildren().addAll(shipmentId, shipmentDate, shipmentStatus, trackingNumber, changeStatusButton);
                tilePane.getChildren().add(shipmentBox);
            }
        });
    }

    private void changeShipmentStatus(String shipmentId) {
        try {
            String newStatus = getNewStatusFromUser();

            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("id", shipmentId);
            parameters.put("status", newStatus);
            String response = HttpRequest.put("/shipments", parameters);

            if (response.equals("OK")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Status updated successfully!");
                alert.show();
                loadShipments();  // reload shipments to reflect the change
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update status!");
                alert.show();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNewStatusFromUser() {
        List<String> choices = Arrays.asList("Processessing", "Shipped", "Delivered", "Cancelled");

        // create ChoiceDialog with default value set to the first item in the choices list
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Change Shipment Status");
        dialog.setHeaderText("Select the new status for the shipment:");
        dialog.setContentText("Choose status:");

        // dialog.setGraphic(new ImageView(this.getClass().getResource("status_icon.png").toString()));

        // show dialog and capture the user response
        Optional<String> result = dialog.showAndWait();

        // ccheck if response was received
        if (result.isPresent())
            return result.get(); // return selected status
        else
            return null; // or handle the case where no input was provided
    }
	
	@FXML
    void handleDashboardEmployeeButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardEmployee();
    }
	
	@FXML
    void handleProductsButtonAction(ActionEvent event) {
        Main.switchToSceneProducts();
    }
	
	@FXML
    void handleShippingButtonAction(ActionEvent event) {
		Main.switchToSceneShipping();
    }
	
	@FXML
    void handleFeedbackEmployeeButtonAction(ActionEvent event) {
        Main.switchToSceneFeedbackEmployee();
    }
	
	@FXML
    void handleSignoutButtonAction(ActionEvent event) {
		Main.switchToSceneStartUp();
    }
}
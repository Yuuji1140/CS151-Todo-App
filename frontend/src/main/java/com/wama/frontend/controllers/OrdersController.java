package com.wama.frontend.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wama.frontend.HttpRequest;
import com.wama.frontend.LoggedInUser;
import com.wama.frontend.Main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OrdersController
{
	LoggedInUser user = LoggedInUser.getInstance();
	HashMap<String, HashMap<String, String>> orders;
	HashMap<String, HashMap<String, String>> shipments;
	UpdaterThread productsUpdater;
	UpdaterThread ordersUpdater;

	@FXML
	private ScrollPane mainContent;

	@FXML
	private TilePane tilePane;

	public void initialize() throws IOException {
		//loadOrders();
		UpdaterThread.createNew(this::loadOrders, 5);
		UpdaterThread.createNew(this::loadShipments, 5);
	}

	@FXML
	private void loadShipments(){
		try {
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("customer_id", user.getCompanyId());
			String response = HttpRequest.get("/shipments", parameters);
			this.shipments = parseProductData(response);
			//System.out.println(this.shipments);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void loadOrders(){
		try {
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("id", "all");
			parameters.put("customer_id", user.getCompanyId());
			String response = HttpRequest.get("/orders", parameters);
			this.orders = parseProductData(response);
			displayOrders();
			//System.out.println(this.orders);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, HashMap<String, String>> parseProductData(String response) {
		HashMap<String, HashMap<String, String>> productData = new HashMap<>();

		// regular expression pattern to match key-value pairs
		String outerPattern = "(\\d+)=(\\{[^}]*\\})";
		Pattern outerRegex = Pattern.compile(outerPattern);
		Matcher outerMatcher = outerRegex.matcher(response);

		while (outerMatcher.find()) {
			HashMap<String, String> detailsMap = new HashMap<>();
			String[] keyValuePairs = outerMatcher.group(1).split(",\\s+");
			String details = outerMatcher.group(2);

			String innerPattern = "(\\w+)=([^,}]+)";
			Pattern innerRegex = Pattern.compile(innerPattern);
			Matcher innerMatcher = innerRegex.matcher(details);
			while (innerMatcher.find()) {
				String key = innerMatcher.group(1);
				String value = innerMatcher.group(2);
				detailsMap.put(key, value);
			}
			productData.put(keyValuePairs[0], detailsMap);
		}
		return productData;
	}

	private void displayOrders() {
	    Platform.runLater(() -> {
	        tilePane.getChildren().clear();
	        
	        for (HashMap.Entry<String, HashMap<String, String>> entry : orders.entrySet()) {
	            HashMap<String, String> order = entry.getValue();

	            VBox orderBox = new VBox(10);
	            orderBox.getStyleClass().add("order-box");
	            orderBox.setPadding(new Insets(10));

	            Text orderId = new Text("Order ID: " + order.get("id"));
	            Text orderDate = new Text("Order Date: " + order.get("order_date"));
	            Text orderTotal = new Text("Total: $" + order.get("total"));
	            Text orderStatus = new Text("Status: " + order.get("status"));
	            Button detailsButton = new Button("View Details");

	            orderBox.getChildren().addAll(orderId, orderDate, orderTotal, orderStatus, detailsButton);
	            
	            detailsButton.setOnAction(event -> showOrderDetails(order));

	            tilePane.getChildren().add(orderBox);
	        }
	    });
	}

	private void showOrderDetails(HashMap<String, String> order) {
	    Stage detailsStage = new Stage();
	    VBox detailsLayout = new VBox(10);
	    detailsLayout.setPadding(new Insets(10));

	    Text itemHeader = new Text("Items in Order:");
	    detailsLayout.getChildren().add(itemHeader);

	    if (order.containsKey("items")) {
	        String[] items = order.get("items").split(";");
	        for (String item : items) {
	            Text itemDetail = new Text(item);
	            detailsLayout.getChildren().add(itemDetail);
	        }
	    }

	    Scene detailsScene = new Scene(detailsLayout, 400, 200);
	    detailsStage.setScene(detailsScene);
	    detailsStage.initModality(Modality.APPLICATION_MODAL);
	    detailsStage.show();
	}

	@FXML
	void handleDashboardCustomerButtonAction(ActionEvent event) {
		Main.switchToSceneDashboardCustomer();
	}

	@FXML
	void handleOrdersButtonAction(ActionEvent event) {
		Main.switchToSceneOrders();
	}
	
	@FXML
	void handleCatalogButtonAction(ActionEvent event) {
		Main.switchToSceneCatalog();
	}

	@FXML
	void handleFeedbackCustomerButtonAction(ActionEvent event) {
		Main.switchToSceneFeedbackCustomer();
	}
	
	@FXML
	void handleSignoutButtonAction(ActionEvent event) {
		Main.switchToSceneStartUp();
	}
}
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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


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
		//https://stackoverflow.com/questions/21160754/run-javafx-controller-in-a-separate-thread
		Platform.runLater(() -> {
			tilePane.getChildren().clear();

			for (HashMap.Entry<String, HashMap<String, String>> entry : orders.entrySet()) {
				HashMap<String, String> order = entry.getValue();
				// Find the order id in the shipments:

				VBox orderBox = new VBox(10);
				orderBox.getStyleClass().add("order-box");
				orderBox.setPadding(new Insets(10));

				Text orderId = new Text("Order ID: " + order.get("id"));
				Text orderDate = new Text("Order Date: " + order.get("order_date"));
				Text orderTotal = new Text("Total: $" + order.get("total"));
				Text orderStatus = new Text("Status: " + order.get("status"));

				orderBox.getChildren().addAll(orderId, orderDate, orderTotal, orderStatus);
				if (orderStatus.getText().equalsIgnoreCase("Status: Shipped") ||
						orderStatus.getText().equalsIgnoreCase("Status: Delivered")) {
					String trackingNumber;
					// For each shipment
					for (HashMap.Entry<String, HashMap<String, String>> shipmentEntry : shipments.entrySet()) {
						HashMap<String, String> shipment = shipmentEntry.getValue();
						if (shipment.get("order_id").equals(order.get("id"))) {
							trackingNumber = shipment.get("tracking_number");
							Text shipmentText = new Text("Shipment: " + shipment.get("status") + "\nTracking Number: " + trackingNumber);
							orderBox.getChildren().add(shipmentText);
						}
					}
				}

				tilePane.getChildren().add(orderBox);
			}
		});
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
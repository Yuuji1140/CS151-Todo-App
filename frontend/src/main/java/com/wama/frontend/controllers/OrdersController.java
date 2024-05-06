package com.wama.frontend.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wama.frontend.HttpRequest;
import com.wama.frontend.LoggedInUser;
import com.wama.frontend.Main;

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
	@FXML
	private ScrollPane mainContent;

	@FXML
	private TilePane tilePane;

	public void initialize() throws IOException {
		loadOrders();
		loadShipments();
		displayOrders();
	}

	@FXML
	private void loadShipments() throws IOException {
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("company_id", user.getCompanyId());
		String response = HttpRequest.get("/shipments", parameters);
		this.shipments = parseProductData(response);
		//System.out.println(this.shipments);
	}

	@FXML
	private void loadOrders() throws IOException {
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("id", "all");
		parameters.put("company_id", user.getCompanyId());
		String response = HttpRequest.get("/orders", parameters);
		this.orders = parseProductData(response);
		//System.out.println(this.orders);
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

			orderBox.getChildren().addAll(orderId, orderDate, orderTotal, orderStatus);
			tilePane.getChildren().add(orderBox);
		}
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
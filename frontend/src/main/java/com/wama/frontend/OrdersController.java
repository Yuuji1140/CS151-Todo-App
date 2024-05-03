package com.wama.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrdersController
{
	LoggedInUser user = LoggedInUser.getInstance();
	HashMap<String, HashMap<String, String>> orders;
	HashMap<String, HashMap<String, String>> shipments;

	public void initialize() throws IOException {
		loadOrders();
		loadShipments();
	}

	@FXML
	private void loadShipments() throws IOException {
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("company_id", user.getCompanyId());
		String response = HttpRequest.get("http://localhost:9876/shipments", parameters);
		this.shipments = parseProductData(response);
		System.out.println(this.shipments);
	}

	@FXML
	private void loadOrders() throws IOException {
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("id", "all");
		parameters.put("company_id", user.getCompanyId());
		String response = HttpRequest.get("http://localhost:9876/orders", parameters);
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
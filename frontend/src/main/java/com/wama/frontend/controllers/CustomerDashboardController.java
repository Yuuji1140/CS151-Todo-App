package com.wama.frontend.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wama.frontend.HttpRequest;
import com.wama.frontend.LoggedInUser;
import com.wama.frontend.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class CustomerDashboardController
{
	@FXML private Label labelDate;
	@FXML private Label labelName;
    private LoggedInUser user = LoggedInUser.getInstance();
    
    @FXML private LineChart<String, Number> lineChart;
    @FXML private NumberAxis yAxis;
    @FXML private CategoryAxis xAxis;
	
	public void initialize() {
		LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy");
        String textDate = currentDate.format(formatter);
        
		labelDate.setText(textDate);
		labelName.setText("Hey, " + user.getUsername() + "!");
		
		loadAndDisplayOrders();
	}
	
	private void loadAndDisplayOrders() {
	    try {
	        HashMap<String, String> parameters = new HashMap<>();
	        parameters.put("id", "all");
	        parameters.put("customer_id", user.getCompanyId());
	        String response = HttpRequest.get("/orders", parameters);
	        HashMap<String, HashMap<String, String>> orders = parseProductData(response);

	        XYChart.Series<String, Number> series = new XYChart.Series<>();
	        //series.setName("Daily Revenue");
	        lineChart.setLegendVisible(false);

	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd");

	        // calculate date range for last 30 days
	        LocalDate endDate = LocalDate.now();
	        LocalDate startDate = endDate.minusDays(29);

	        for (String orderId : orders.keySet()) {
	            String orderDate = orders.get(orderId).get("order_date");
	            Double total = Double.parseDouble(orders.get(orderId).get("total"));

	            // parse order date from java.sql.Timestamp
	            Timestamp timestamp = Timestamp.valueOf(orderDate);
	            LocalDate date = timestamp.toLocalDateTime().toLocalDate();

	            // include only data within last 30 days
	            if ((date.isEqual(startDate) || date.isAfter(startDate)) && (date.isEqual(endDate) || date.isBefore(endDate))) {
	                String formattedDate = date.format(dateFormatter);
	                series.getData().add(new XYChart.Data<>(formattedDate, total));
	            }
	        }

	        lineChart.getData().clear();
	        lineChart.getData().add(series);
	    }
	    catch (IOException e) {
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
    void handleFeedbackCustomerBackButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardCustomer();
    }
	
	@FXML
    void handleSignoutButtonAction(ActionEvent event) {
		Main.switchToSceneStartUp();
    }
}
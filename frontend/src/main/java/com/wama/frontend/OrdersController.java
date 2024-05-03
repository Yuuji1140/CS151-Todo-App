package com.wama.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class OrdersController
{
	LoggedInUser user = LoggedInUser.getInstance();

	public void initialize() {
		loadCatalog();
	}

	@FXML
	private void loadCatalog() {
		// Print everything about the user for debug first
		System.out.println(user);
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
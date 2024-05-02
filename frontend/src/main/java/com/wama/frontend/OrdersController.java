package com.wama.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class OrdersController
{
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
package com.wama.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardController
{
	@FXML
    void handleSignoutButtonAction(ActionEvent event) {
		// TODO: Handle logging user out here
		Main.switchToSceneStartUp();
    }
	
	@FXML
    void handleDashboardEmployeeButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardEmployee();
    }
	
	@FXML
    void handleDashboardCustomerButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardCustomer();
    }
	
	@FXML
    void handleProductsButtonAction(ActionEvent event) {
		Main.switchToSceneProducts();
    }
	
	@FXML
    void handleOrdersButtonAction(ActionEvent event) {
        Main.switchToSceneOrders();
    }
	
	@FXML
    void handleTrackingButtonAction(ActionEvent event) {
		Main.switchToSceneTracking();
    }
	
	@FXML
    void handleFeedbackCustomerButtonAction(ActionEvent event) {
        Main.switchToSceneFeedbackCustomer();
    }
	
	@FXML
    void handleFeedbackEmployeeButtonAction(ActionEvent event) {
        Main.switchToSceneFeedbackEmployee();
    }
	
	@FXML
    void handleFeedbackCustomerBackButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardCustomer();
    }
	
	@FXML
    void handleFeedbackEmployeeBackButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardEmployee();
    }
}
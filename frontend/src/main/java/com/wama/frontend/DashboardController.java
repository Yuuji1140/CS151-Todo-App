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
    void handleEmployeeDashboardButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardEmployee();
    }
	
	@FXML
    void handleCustomerDashboardButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardCustomer();
    }
	
	@FXML
    void handleOrdersButtonAction(ActionEvent event) {
        Main.switchToSceneOrders();
    }
	
	@FXML
    void handleProductsButtonAction(ActionEvent event) {
		Main.switchToSceneTracking();
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
    void handleFeedbackCustomerBackButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardCustomer();
    }
	
	@FXML
    void handleFeedbackEmployeeButtonAction(ActionEvent event) {
        Main.switchToSceneFeedbackEmployee();
    }
	
	@FXML
    void handleFeedbackEmployeeBackButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardEmployee();
    }
}
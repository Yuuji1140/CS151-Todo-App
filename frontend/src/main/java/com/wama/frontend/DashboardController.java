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
        Main.switchToSceneEmployeeDashboard();
    }
	
	@FXML
    void handleCustomerDashboardButtonAction(ActionEvent event) {
        Main.switchToSceneCustomerDashboard();
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
    void handleFeedbackButtonAction(ActionEvent event) {
        Main.switchToSceneFeedback();
    }
}
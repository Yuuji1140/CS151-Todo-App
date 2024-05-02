package com.wama.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FeedbackController
{
	@FXML
    void handleFeedbackCustomerBackButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardCustomer();
    }
	
	@FXML
    void handleFeedbackEmployeeBackButtonAction(ActionEvent event) {
		Main.switchToSceneDashboardEmployee();
    }
}
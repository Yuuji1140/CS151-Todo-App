package com.wama.frontend.controllers;

import com.wama.frontend.Main;

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
package com.wama.frontend.controllers;

import com.wama.frontend.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ShippingController
{
	@FXML
    void handleDashboardEmployeeButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardEmployee();
    }
	
	@FXML
    void handleProductsButtonAction(ActionEvent event) {
        Main.switchToSceneProducts();
    }
	
	@FXML
    void handleShippingButtonAction(ActionEvent event) {
		Main.switchToSceneShipping();
    }
	
	@FXML
    void handleFeedbackEmployeeButtonAction(ActionEvent event) {
        Main.switchToSceneFeedbackEmployee();
    }
	
	@FXML
    void handleSignoutButtonAction(ActionEvent event) {
		Main.switchToSceneStartUp();
    }
}
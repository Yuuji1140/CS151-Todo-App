package com.wama.frontend.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.wama.frontend.LoggedInUser;
import com.wama.frontend.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController
{
	@FXML private Label labelDate;
	@FXML private Label labelName;
    private LoggedInUser user = LoggedInUser.getInstance();
	
	public void initialize() {
		LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy");
        String textDate = currentDate.format(formatter);
        
		labelDate.setText(textDate);
		labelName.setText("Hey, " + user.getUsername() + "!");
	}
	
	@FXML
    void handleDashboardCustomerButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardCustomer();
    }
	
	@FXML
    void handleDashboardEmployeeButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardEmployee();
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
    void handleProductsButtonAction(ActionEvent event) {
        Main.switchToSceneProducts();
    }
	
	@FXML
    void handleShippingButtonAction(ActionEvent event) {
		Main.switchToSceneShipping();
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
	
	@FXML
    void handleSignoutButtonAction(ActionEvent event) {
		Main.switchToSceneStartUp();
    }
}
package com.wama.frontend.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.wama.frontend.LoggedInUser;
import com.wama.frontend.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmployeeDashboardController
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
    void handleFeedbackEmployeeBackButtonAction(ActionEvent event) {
		Main.switchToSceneDashboardEmployee();
    }
	
	@FXML
    void handleSignoutButtonAction(ActionEvent event) {
		Main.switchToSceneStartUp();
    }
}
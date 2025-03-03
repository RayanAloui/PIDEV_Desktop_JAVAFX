package esprit.tn.controllers;

import esprit.tn.entities.Session;
import esprit.tn.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Auth {


    @FXML
    private Button connectButton;

    @FXML
    private TextField field1;

    @FXML
    private TextField field2;

    @FXML
    private TextField field3;

    @FXML
    private TextField field4;

    private String TOKEN;
    public void setToken(String token) {
        this.TOKEN = token;
    }

    @FXML
    void login(ActionEvent event) {


        // Concatenate the fields' text to create the user input code
        String userInput = field1.getText() + field2.getText() + field3.getText() + field4.getText();

        // Convert the user's token to a String for comparison
        String token = TOKEN;

        // Compare the concatenated input with the user's token
        if (token.equals(userInput)) {
            // Navigate to home.fxml if the token matches
            navigateToPage(event, "/home.fxml");
        } else {
            // Show an alert if the code is invalid
            showAlert(AlertType.ERROR, "Invalid Code", "The code entered is incorrect.");
        }
    }

    // Navigate to another page (to be implemented)
    private void navigateToPage(ActionEvent event, String fxmlFile) {
        // Implement navigation logic here
        System.out.println("Navigating to " + fxmlFile);
    }

    // Show an alert (generic method)
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
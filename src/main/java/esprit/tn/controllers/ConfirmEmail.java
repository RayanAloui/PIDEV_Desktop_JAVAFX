package esprit.tn.controllers;

import esprit.tn.entities.Session;
import esprit.tn.entities.User;
import esprit.tn.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class ConfirmEmail {

    @FXML
    private TextField code;

    @FXML
    private Label emaillabel;

    public User currentUser;

    public void initialize() {
        Session session = Session.getInstance();
        if (session.isLoggedIn()) {
            currentUser = session.getCurrentUser();
            emaillabel.setText(currentUser.getEmail());
        }
    }

    @FXML
    void handleConfirmButtonClick(ActionEvent event) {
        try {
            // Validate the code input
            int inputCode = Integer.parseInt(code.getText());

            if (currentUser.getNumberVerification() == inputCode) {
                // If the code matches, confirm the email and show a success message
                showAlert("Success", "Your account is now confirmed.", AlertType.INFORMATION);
                showAlert("Two-Factor Authentication is Enabled", "Each login will now require a verification code.", AlertType.INFORMATION);
                Random random = new Random();
                int verificationNumber = 100000 + random.nextInt(900000);

                Random random1 = new Random();
                int token = 1000 + random1.nextInt(9000);


                currentUser.setNumberVerification(verificationNumber);
                currentUser.setToken(token);

                UserService userService = new UserService();
                userService.modifier(currentUser, currentUser.getId());
                userService.confirm(currentUser.getId());


            } else {
                // If the code is incorrect, show an error message
                showAlert("Invalid Code", "The verification code is invalid. Please try again.", AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            // If the user entered a non-numeric code, show an error message
            showAlert("Invalid Input", "Please enter a valid numeric code.", AlertType.ERROR);
        }
    }

    // Helper method to display alerts
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void back(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editProfile.fxml")); // Replace with actual path
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to load the user list page");
            errorAlert.showAndWait();
        }
    }














}

package esprit.tn.controllers;

import esprit.tn.entities.User;
import esprit.tn.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class CheckValidationNumber {

    @FXML
    private TextField verificationCodeField;

    @FXML
    private Button verifyButton;

    private User user; // Store the user instance

    @FXML
    void setUser(User u) {
        this.user = u; // Store the User object

    }

    @FXML
    void handleVerifyButton(ActionEvent event) {
        if (user == null) {
            showAlert("Error", "No user data provided!", Alert.AlertType.ERROR);
            return;
        }

        String number = String.valueOf(user.getNumberVerification()); // Convert int to String if needed

        // Check if the verification code entered by the user matches the one from the User object
        if (verificationCodeField.getText().equals(number)) {
            showAlert("Success", "You can generate a new password now!", Alert.AlertType.INFORMATION);

            Random random = new Random();
            int verificationNumber = 100000 + random.nextInt(900000);

            user.setNumberVerification(verificationNumber);

            UserService userService = new UserService();
            userService.modifier(user, user.getId());

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/newPassword.fxml"));
                Parent root = loader.load(); // Load the FXML first

                // Get the controller and pass the user object
                NewPassword newPasswordController = loader.getController();
                newPasswordController.setUser(user);

                // Get the current stage and switch scenes
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Could not load the New Password page!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Incorrect verification code. Please try again.", Alert.AlertType.ERROR);
        }
    }


    // Utility method to show alert messages
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

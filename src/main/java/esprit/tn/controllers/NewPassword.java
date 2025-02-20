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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

public class NewPassword {

    @FXML
    private Label label;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Button okButton;

    private User user; // Store the user instance
    private final UserService userService = new UserService(); // Initialize once

    @FXML
    public void setUser(User u) {
        this.user = u; // Store the User object
        if (user != null) {
            label.setText("Hello " + user.getName() + ", you can generate a new password here:");
        }
    }

    @FXML
    private void handleOkButton(ActionEvent event) {
        if (user == null) {
            showAlert("Error", "User is not set!", Alert.AlertType.ERROR);
            return;
        }

        String password = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Password fields cannot be empty!", Alert.AlertType.ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match!", Alert.AlertType.ERROR);
            return;
        }

        user.setPassword(password);
        userService.modifier(user, user.getId());
        showAlert("Success", "Password updated successfully!", Alert.AlertType.INFORMATION);

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


            stage.setScene(new Scene(root));


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the users page");
            alert.setContentText("An error occurred while trying to navigate back to the user list.");
            alert.showAndWait();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

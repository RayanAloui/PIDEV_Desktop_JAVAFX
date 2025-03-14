package esprit.tn.controllers;

import esprit.tn.entities.Notification;
import esprit.tn.services.NotificationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import esprit.tn.services.UserService;
import esprit.tn.entities.Session;
import esprit.tn.entities.User;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.prefs.Preferences;

public class ChangePWD {

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    private Session session = Session.getInstance();
    private User currentUser;

    @FXML
    void handleCancelButtonClick(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editProfile.fxml"));
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
    @FXML
    void updatePWD(ActionEvent event) {
        // Check if the user is logged in
        if (session.isLoggedIn()) {
            currentUser = session.getCurrentUser();

            // Validate the current password
            String currentPassword = currentPasswordField.getText();

            // Encrypt the current password before comparison
            UserService userService = new UserService();
            String cryptedCurrentPassword = userService.CRYPTE(currentPassword);

            // Check if the entered current password matches the stored password
            if (!cryptedCurrentPassword.equals(currentUser.getPassword())) {
                showAlert(AlertType.ERROR, "Incorrect Current Password", "The current password you entered is incorrect.");
                return;
            }

            // Validate the new password
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // Check if the new passwords match
            if (!newPassword.equals(confirmPassword)) {
                showAlert(AlertType.ERROR, "Password Mismatch", "The new passwords do not match.");
                return;
            }

            // Check if the password meets the length requirement
            if (newPassword.length() < 8) {
                showAlert(AlertType.ERROR, "Invalid Password", "Password must be at least 8 characters long.");
                return;
            }

            // Encrypt the new password before updating
            String cryptedNewPassword = userService.CRYPTE(newPassword);  // Encrypt the new password

            currentUser.setPassword(cryptedNewPassword);  // Update the password field of the current user

            // Update the user in the database
            userService.modifier(currentUser, currentUser.getId());

            // Create and send a notification
            Notification notification = new Notification();
            notification.setUsername(currentUser.getName());
            notification.setActivite("Changed password");
            String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            notification.setHeure(formattedTime);
            notification.setDate(Date.valueOf(LocalDate.now()));

            NotificationService notificationService = new NotificationService();
            notificationService.ajouter(notification);

            // Show success message
            showAlert(AlertType.INFORMATION, "Success", "Password updated successfully!");

            // Optionally clear the fields after the update
            currentPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
        } else {
            showAlert(AlertType.ERROR, "Session Expired", "You must be logged in to update your password.");
        }

        // Clear session and navigate back to login screen
        Session session = Session.getInstance();
        session.clearSession();
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


    // Utility method to show alerts
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

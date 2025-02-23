package esprit.tn.controllers;

import esprit.tn.entities.EmailService;
import esprit.tn.entities.Notification;
import esprit.tn.services.NotificationService;
import esprit.tn.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import esprit.tn.entities.User;
import esprit.tn.entities.Session;
import esprit.tn.main.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfile {

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField confirmNewPasswordField;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField  newPasswordField;

    private User currentUser;

    @FXML
    public void initialize() {
        // Retrieve the current session
        Session session = Session.getInstance();

        // Check if a user is logged in
        if (session.isLoggedIn()) {
            // Get the logged-in user
            currentUser = session.getCurrentUser();

            // Populate the form fields with the user's current information
            nameField.setText(currentUser.getName());
            surnameField.setText(currentUser.getSurname());
            telephoneField.setText(currentUser.getTelephone());
            emailField.setText(currentUser.getEmail());
        } else {
            // If no user is logged in, redirect to the login page
            redirectToLoginPage();
        }
    }

    @FXML
    void handleSaveButtonClick(ActionEvent event) {
        // Validate inputs before updating the user's information
        if (!validateInput()) {
            return; // If validation fails, stop the update process
        }

        // Update the user's information
        currentUser.setName(nameField.getText());
        currentUser.setSurname(surnameField.getText());
        currentUser.setTelephone(telephoneField.getText());
        currentUser.setEmail(emailField.getText());

        UserService userService = new UserService();
        userService.modifier(currentUser, currentUser.getId());



        //
        Notification notification = new Notification();

        notification.setUsername(currentUser.getName());
        notification.setActivite("Edit Profile");
        String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        notification.setHeure(formattedTime);
        notification.setDate(Date.valueOf(LocalDate.now()));


        NotificationService notificationService = new NotificationService();
        notificationService.ajouter(notification);
        //

        showAlert(Alert.AlertType.INFORMATION, "Updated", "Your information has been updated successfully.");
    }

    @FXML
    void handleCancelButtonClick(ActionEvent event) {
        // Navigate back to the previous page (e.g., the profile page)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        // Validate name (must be alphabetic only)
        if (!nameField.getText().matches("[A-Za-z]+")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name must only contain letters.");
            return false;
        }

        // Validate surname (must be alphabetic only)
        if (!surnameField.getText().matches("[A-Za-z]+")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Surname must only contain letters.");
            return false;
        }

        // Validate telephone (must be 8 digits)
        if (!telephoneField.getText().matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Telephone number must be exactly 8 digits.");
            return false;
        }

        // Validate email format
        String email = emailField.getText();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid email address.");
            return false;
        }

        return true;
    }



    private void redirectToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Helper method to display alert messages
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handleChangePasswordLinkClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/changePWD.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void SendMail(ActionEvent actionEvent) {
        try {
            EmailService mailService = new EmailService();
            String to=currentUser.getEmail();
            String code = String.valueOf(currentUser.getNumberVerification());
            mailService.envoyerEmail(to, "Code de Confirmation", code);
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/confirmEmail.fxml")); // Replace with actual path
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

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

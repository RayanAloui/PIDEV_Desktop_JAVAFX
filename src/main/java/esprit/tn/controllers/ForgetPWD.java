package esprit.tn.controllers;

import com.google.protobuf.Message;
import esprit.tn.entities.MailService;
import esprit.tn.entities.Session;
import esprit.tn.entities.SmsService;
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
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import  esprit.tn.entities.User;
public class ForgetPWD {



    @FXML
    private TextField emailField;



    @FXML
    void GoToLogin(ActionEvent event) {
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


    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void sendMailButton(ActionEvent event) {
        UserService userService = new UserService();

        if (!userService.telephoneExists(emailField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Phone number doesn't exist",
                    "The phone number you entered is not registered. Please use a different number.");
            return;
        }


        User user = userService.telephoneExists1(emailField.getText()); // Retrieve user
        String phoneNumber = user.getTelephone().trim().replaceAll("\\s+", ""); // Remove spaces
        String recipientPhoneNumber = "+216" + phoneNumber;


        int verificationCode = user.getNumberVerification();
        String verificationCodeString = String.valueOf(verificationCode);  // Convert int to String

        try {
            SmsService.send(recipientPhoneNumber, verificationCodeString);

            showAlert(Alert.AlertType.INFORMATION, "Success", "SMS Sent",
                    "A verification code has been sent to " + recipientPhoneNumber);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to Send SMS",
                    "An error occurred while sending the SMS. Please try again.");
            e.printStackTrace();
        }

        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CheckValidationNumber.fxml"));
            Parent root = loader.load();

            // Get the controller instance
            CheckValidationNumber checkValidationController = loader.getController();

            // Pass the user to the new page
            checkValidationController.setUser(user);

            // Show the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load verification page",
                    "An error occurred while navigating to the verification page.");
        }
    }









}

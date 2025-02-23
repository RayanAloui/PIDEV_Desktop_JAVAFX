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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class doubleAuth {
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

    @FXML
    void login(ActionEvent event) {
        // Get the current user from the session
        Session session = Session.getInstance();
        User u = session.getCurrentUser();

        // Concatenate the values from the four fields
        String userInput = field1.getText() + field2.getText() + field3.getText() + field4.getText();

        // Convert the token from the user (assuming it's an integer or long) to a string
        String token = String.valueOf(u.getToken());

        // Compare the concatenated input with the token
        if (userInput.equals(token)) {
            Random random1 = new Random();
            int t = 1000 + random1.nextInt(9000);
            u.setToken(t);

            UserService userService = new UserService();
            userService.modifier(u, u.getId());


            u.setToken(t);
            // If the input matches the token, proceed (e.g., navigate to the next page)
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml")); // Replace with actual path
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Failed to load the user list page");
                errorAlert.showAndWait();
            }
        } else {
            // If the input does not match, show an error alert
            showAlert(AlertType.ERROR, "Invalid Code", "The code entered is incorrect.");
        }
    }


    // Show an alert (generic method)
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML

    private void onField1KeyReleased() {
        if (field1.getText().length() == 1) {
            field2.requestFocus();
        }
    }

    @FXML
    private void onField2KeyReleased() {
        if (field2.getText().length() == 1) {
            field3.requestFocus();
        }
    }

    @FXML
    private void onField3KeyReleased() {
        if (field3.getText().length() == 1) {
            field4.requestFocus();
        }
    }

    @FXML
    private void onField4KeyReleased() {
        if (field4.getText().length() == 1) {
         
            connectButton.requestFocus();
        }
    }
}

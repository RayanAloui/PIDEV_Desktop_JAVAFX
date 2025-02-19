package esprit.tn.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import esprit.tn.entities.User;
import esprit.tn.entities.Session;
import javafx.stage.Stage;

import java.io.IOException;

public class Home {

    @FXML
    private Label email;

    @FXML
    private Label name;

    @FXML
    private Button profileButton;

    @FXML
    public void initialize() {
        // Retrieve the current session
        Session session = Session.getInstance();

        // Check if a user is logged in
        if (session.isLoggedIn()) {
            // Get the logged-in user
            User user = session.getCurrentUser();

            // Populate the labels with user data
            email.setText(user.getEmail());
            name.setText(user.getName());
        } else {
            // If no user is logged in, show a message or redirect to the login page
            email.setText("No user logged in");
            name.setText("Guest");
        }
    }

    @FXML
    void handleProfileButtonClick(ActionEvent event) {
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

    public void handleLogoutButtonClick(ActionEvent actionEvent) {
        // Clear the session
        Session session = Session.getInstance();
        session.clearSession();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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
}
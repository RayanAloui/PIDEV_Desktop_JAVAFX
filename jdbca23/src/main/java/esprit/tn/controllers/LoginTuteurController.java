package esprit.tn.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import esprit.tn.main.databaseconnection1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginTuteurController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField cinField;
    @FXML
    private Button btnLogin;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String cin = cinField.getText();

        if (authenticateTuteur(email, cin)) {
            openTuteurDashboard();
        } else {
            errorLabel.setText("Email ou CIN incorrect !");
        }
    }

    private boolean authenticateTuteur(String email, String cin) {
        Connection conn = databaseconnection1.getConnection();
        String query = "SELECT * FROM tuteurs WHERE email = ? AND cinT = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, cin);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Si un résultat est trouvé, c'est valide
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openTuteurDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TuteurDashbord.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Espace Tuteur");
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) btnLogin.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(ActionEvent actionEvent) {
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



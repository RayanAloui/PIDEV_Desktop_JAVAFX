package esprit.tn.controllers;

import esprit.tn.entities.Orphelin;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import esprit.tn.services.ServiceOrphelin;

import java.io.IOException;
import java.sql.SQLException;

public class LoginOrphelinController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField idField;
    @FXML
    private Button btnLoginOrphelin;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) throws SQLException {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        int id = Integer.parseInt(idField.getText());

        Orphelin orphelin = authenticateOrphelin(nom, prenom, id); // Vérifier l’authentification et récupérer l'orphelin

        if (orphelin != null) {
            openOrphelinDashboard(orphelin); // Transmettre l'orphelin authentifié
        } else {
            errorLabel.setText("Identifiants incorrects !");
        }
    }


    private Orphelin authenticateOrphelin(String nom, String prenom, int id) throws SQLException {
        ServiceOrphelin serviceOrphelin = new ServiceOrphelin();
        Orphelin orphelin = serviceOrphelin.getOrphelinById(id);

        if (orphelin != null && orphelin.getNomO().equals(nom) && orphelin.getPrenomO().equals(prenom)) {
            return orphelin;
        }
        return null;
    }


    private void openOrphelinDashboard(Orphelin orphelin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/OrphelinDashbord.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur du dashboard et lui transmettre l'ID du tuteur
            OrphelinDashboardController controller = loader.getController();
            controller.setIdTuteur(orphelin.getIdTuteur()); // Transmettre l'ID du tuteur
            controller.setOrphelinActuel(orphelin);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Espace Orphelin");
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) btnLoginOrphelin.getScene().getWindow();
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


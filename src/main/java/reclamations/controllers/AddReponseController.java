package reclamations.controllers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import reclamations.entities.Reclamation;
import reclamations.entities.Reponse;
import reclamations.services.ReclamationService;
import reclamations.services.ReponseService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.LocalDate;

public class AddReponseController {

    @FXML
    private TextArea description;

    @FXML
    private Label descriptionError;

    @FXML
    private ChoiceBox<String> statut;

    @FXML
    private Label statutError;

    /**
     * Initialisation des éléments de l'interface utilisateur
     * Masquage des messages d'erreur par défaut.
     */
    @FXML
    public void initialize() {
        descriptionError.setVisible(false);
        statutError.setVisible(false);
        descriptionError.setText("Description is required");
        statutError.setText("Statut is required");

        description.clear();
        statut.getSelectionModel().clearSelection();
    }

    /**
     * Méthode de navigation vers la page d'affichage des réponses
     */
    @FXML
    void GoToAfficherReponses(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherreponse.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the response page");
            alert.showAndWait();
        }
    }

    /**
     * Méthode pour ajouter une réponse à la base de données après validation
     */
    @FXML
    void addReponse(ActionEvent event) {
        boolean isValid = true;

        // Réinitialiser les messages d'erreur
        descriptionError.setVisible(false);
        statutError.setVisible(false);

        // Validation de la description
        if (description.getText().trim().isEmpty()) {
            descriptionError.setText("Description is required");
            descriptionError.setVisible(true);
            isValid = false;
        }

        // Validation du statut
        if (statut.getValue() == null) {
            statutError.setText("Statut is required");
            statutError.setVisible(true);
            isValid = false;
        }

        // Si la validation échoue, arrêter l'exécution
        if (!isValid) {
            return;
        }

        // Créer une nouvelle réponse
        Reponse reponse = new Reponse();
        reponse.setDescription(description.getText());
        reponse.setStatut(statut.getValue());
        reponse.setDate(Date.valueOf(LocalDate.now()));

        // Ajouter la réponse à la base de données
        ReponseService reponseService = new ReponseService();
        reponseService.ajouter(reponse);

        // Afficher un message de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Response Added");
        alert.setHeaderText("Response added successfully");
        alert.showAndWait();

        // Rediriger vers la page de la liste des réponses
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherReponse.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to load the response list page");
            errorAlert.showAndWait();
        }
    }
}
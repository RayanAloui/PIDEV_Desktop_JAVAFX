package esprit.tn.controllers;

import esprit.tn.entities.Tuteur;
import javafx.scene.control.ComboBox;
import esprit.tn.services.ServiceTuteur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AjouterTuteurController {

    @FXML
    private TextField adresseId;

    @FXML
    private TextField cinId;

    @FXML
    private TextField nomId;

    @FXML
    private TextField prenomId;

    @FXML
    private TextField telephoneId;

    @FXML
    private TextField emailId;

    @FXML
    private ComboBox<String> disponibiliteId;

    @FXML
    private Label errorCin, errorNom, errorPrenom, errorTelephone, errorAdresse,errorEmail, errorDisponibilite;;

    private final ServiceTuteur serviceTuteur = new ServiceTuteur();


        @FXML
        void initialize() {
            // Initialiser les choix de disponibilité
            disponibiliteId.getItems().addAll("oui", "non");
        }

        @FXML
        void AjouterT(ActionEvent event) {
            // Réinitialisation des erreurs
            errorCin.setText("");
            errorNom.setText("");
            errorPrenom.setText("");
            errorTelephone.setText("");
            errorAdresse.setText("");
            errorDisponibilite.setText("");
            errorEmail.setText("");

            // Récupération des valeurs
            String cin = cinId.getText().trim();
            String nom = nomId.getText().trim();
            String prenom = prenomId.getText().trim();
            String telephone = telephoneId.getText().trim();
            String adresse = adresseId.getText().trim();
            String disponibilite = disponibiliteId.getValue();
            String email = emailId.getText().trim();

            boolean hasError = false;

            // Vérification du CIN
            if (cin.isEmpty()) {
                errorCin.setText("Le CIN est obligatoire !");
                hasError = true;
            } else if (!cin.matches("\\d{8}")) {
                errorCin.setText("Le CIN doit contenir exactement 8 chiffres !");
                hasError = true;
            }

            // Vérification du nom
            if (nom.isEmpty()) {
                errorNom.setText("Le nom est obligatoire !");
                hasError = true;
            } else if (!nom.matches("[a-zA-ZÀ-ÿ\\s-]+")) {
                errorNom.setText("Le nom ne doit contenir que des lettres !");
                hasError = true;
            }

            // Vérification du prénom
            if (prenom.isEmpty()) {
                errorPrenom.setText("Le prénom est obligatoire !");
                hasError = true;
            } else if (!prenom.matches("[a-zA-ZÀ-ÿ\\s-]+")) {
                errorPrenom.setText("Le prénom ne doit contenir que des lettres !");
                hasError = true;
            }

            // Vérification du téléphone
            if (!telephone.matches("\\d{8}")) {
                errorTelephone.setText("Le téléphone doit contenir exactement 8 chiffres !");
                hasError = true;
            }

            // Vérification de l'email
            if (email.isEmpty()) {
                errorEmail.setText("L'email est obligatoire !");
                hasError = true;
            } else if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                errorEmail.setText("Format d'email invalide !");
                hasError = true;
            }

            // Vérification de l'adresse
            if (adresse.isEmpty()) {
                errorAdresse.setText("L'adresse est obligatoire !");
                hasError = true;
            }

            // Vérification de la disponibilité
            if (disponibilite == null || disponibilite.isEmpty()) {
                errorDisponibilite.setText("Veuillez sélectionner une disponibilité !");
                hasError = true;
            }

            // Si une erreur est détectée, arrêter l'ajout
            if (hasError) return;

            // Création et ajout du tuteur
            try {
                Tuteur tuteur = new Tuteur(cin, nom, prenom, telephone, adresse, disponibilite,email);
                serviceTuteur.ajouter(tuteur);

                // Réinitialiser les champs après un ajout réussi
                cinId.clear();
                nomId.clear();
                prenomId.clear();
                telephoneId.clear();
                emailId.clear();
                adresseId.clear();
                disponibiliteId.getSelectionModel().clearSelection(); // Réinitialiser la disponibilité

                // Afficher une alerte de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ajout réussi");
                alert.setHeaderText(null);
                alert.setContentText("Le tuteur a été ajouté avec succès !");
                alert.showAndWait();

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'ajout");
                alert.setHeaderText("Impossible d'ajouter le tuteur !");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }

        @FXML
        void afficherTuteur(ActionEvent event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherTuteur.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Liste des Tuteurs");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}

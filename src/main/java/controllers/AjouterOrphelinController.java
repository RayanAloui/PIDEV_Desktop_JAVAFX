package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ServiceOrphelin;
import entities.Orphelin;
import entities.Tuteur;
import services.ServiceTuteur;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class AjouterOrphelinController {

    @FXML
    private TextField nomField, prenomField, situationScolaireField;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private ComboBox<String> sexeComboBox, tuteurComboBox;

    @FXML
    private Label errorNom, errorPrenom, errorDateNaissance, errorSexe, errorSituationScolaire, errorTuteur;

    private final ServiceOrphelin serviceOrphelin = new ServiceOrphelin(); // Service pour la gestion SQL

    @FXML
    public void initialize() {
        // Initialisation des valeurs du ComboBox
        sexeComboBox.setItems(FXCollections.observableArrayList("M", "F"));

        // Charger les tuteurs depuis la base de données
        chargerTuteurs();
    }

    private void chargerTuteurs() {
        try {
            tuteurComboBox.setItems(FXCollections.observableArrayList(serviceOrphelin.getTuteurs())); // Charge la liste des tuteurs
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des tuteurs : " + e.getMessage());
        }
    }

    @FXML
    private void AjouterO() {
        // Réinitialisation des erreurs
        resetErrors();

        // Récupération des valeurs des champs
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        String sexe = sexeComboBox.getValue();
        String situationScolaire = situationScolaireField.getText().trim();
        String tuteur = tuteurComboBox.getValue();

        boolean isValid = true;

        // Validation des champs obligatoires
        if (nom.isEmpty()) {
            errorNom.setText("Le nom est obligatoire.");
            isValid = false;
        } else if (!Pattern.matches("^[a-zA-ZÀ-ÿ\\s]+$", nom)) {
            errorNom.setText("Le nom ne doit contenir que des lettres et des espaces.");
            isValid = false;
        }

        if (prenom.isEmpty()) {
            errorPrenom.setText("Le prénom est obligatoire.");
            isValid = false;
        } else if (!Pattern.matches("^[a-zA-ZÀ-ÿ\\s]+$", prenom)) {
            errorPrenom.setText("Le prénom ne doit contenir que des lettres et des espaces.");
            isValid = false;
        }

        if (dateNaissance == null) {
            errorDateNaissance.setText("La date de naissance est obligatoire.");
            isValid = false;
        } else if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", dateNaissance.toString())) {
            errorDateNaissance.setText("La date doit être au format YYYY-MM-DD.");
            isValid = false;
        }

        if (sexe == null || (!sexe.equalsIgnoreCase("M") && !sexe.equalsIgnoreCase("F"))) {
            errorSexe.setText("Le sexe doit être 'M' ou 'F'.");
            isValid = false;
        }

        if (tuteur == null || !tuteur.matches("\\d+")) {
            errorTuteur.setText("Veuillez sélectionner un tuteur valide.");
            isValid = false;
        } else {
            int idTuteur = Integer.parseInt(tuteur);
            if (idTuteur <= 0 || !tuteurExiste(idTuteur)) {
                errorTuteur.setText("L'ID du tuteur doit être un entier positif existant.");
                isValid = false;
            }
        }

        if (!isValid) return; // Arrêter l'exécution si une erreur est présente

        // Création de l'objet Orphelin
        Orphelin orphelin = new Orphelin(nom, prenom, dateNaissance.toString(), sexe, situationScolaire, Integer.parseInt(tuteur));

        try {
            serviceOrphelin.ajouter(orphelin);
            afficherMessage("Succès", "Orphelin ajouté avec succès !");
            viderChamps(); // Nettoyer les champs après l'ajout
        } catch (SQLException e) {
            afficherMessage("Erreur", "Impossible d'ajouter l'orphelin : " + e.getMessage());
        }
    }


    private boolean tuteurExiste(int idTuteur) {
        try {
            return serviceOrphelin.tuteurExiste(idTuteur);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du tuteur : " + e.getMessage());
            return false;
        }
    }

    private void resetErrors() {
        errorNom.setText("");
        errorPrenom.setText("");
        errorDateNaissance.setText("");
        errorSexe.setText("");
        errorSituationScolaire.setText("");
        errorTuteur.setText("");
    }

    private void afficherMessage(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void viderChamps() {
        nomField.clear();
        prenomField.clear();
        dateNaissancePicker.setValue(null);
        sexeComboBox.setValue(null);
        situationScolaireField.clear();
        tuteurComboBox.setValue(null);
    }

    @FXML
    void afficherOrphelin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOrphelin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Orphelins");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



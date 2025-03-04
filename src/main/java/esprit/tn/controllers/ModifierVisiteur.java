package esprit.tn.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import esprit.tn.entities.visiteurs;
import esprit.tn.services.VisiteursService;

import java.io.IOException;

public class ModifierVisiteur {

    @FXML
    private TextField adresse;

    @FXML
    private TextField email;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField tel;

    @FXML
    private TextField cin; // 🔹 Correction ici (CIN doit être String)

    private visiteurs visiteur;

    // Méthode pour initialiser les champs avec les données du visiteur
    public void setVisiteur(visiteurs visiteur) {
        this.visiteur = visiteur;
        nom.setText(visiteur.getNom());
        prenom.setText(visiteur.getPrenom());
        email.setText(visiteur.getEmail());
        tel.setText(String.valueOf(visiteur.getTel()));
        adresse.setText(visiteur.getAdresse());
        cin.setText(visiteur.getCin()); // 🔹 Correction ici (CIN est String)
    }

    @FXML
    void ModifierV(ActionEvent event) {
        try {
            // Récupération des valeurs depuis les champs de texte
            String nomValue = nom.getText();
            String prenomValue = prenom.getText();
            String emailValue = email.getText();
            String telValue = tel.getText();
            String cinValue = cin.getText(); // 🔹 Correction ici (CIN est String)
            String adresseValue = adresse.getText();

            // Validation des données
            if (nomValue.isEmpty() || prenomValue.isEmpty() || emailValue.isEmpty() ||
                    adresseValue.isEmpty() || telValue.isEmpty() || cinValue.isEmpty()) {
                showError("Tous les champs doivent être remplis.");
                return;
            }

            if (!cinValue.matches("\\d{8}")) {
                showError("Le CIN doit contenir exactement 8 chiffres.");
                return;
            }

            if (!telValue.matches("\\d{8}")) {
                showError("Le numéro de téléphone doit contenir exactement 8 chiffres.");
                return;
            }

            // Mise à jour des informations du visiteur
            visiteur.setNom(nomValue);
            visiteur.setPrenom(prenomValue);
            visiteur.setEmail(emailValue);
            visiteur.setTel(Integer.parseInt(telValue));
            visiteur.setCin(cinValue); // 🔹 Correction ici
            visiteur.setAdresse(adresseValue);

            // Mise à jour du visiteur dans la base de données
            VisiteursService.getInstance().modifier(visiteur);

            // Message de confirmation
            showInfo("Visiteur modifié avec succès.");

            // Retour à la liste des visiteurs
            retour(event);

        } catch (NumberFormatException e) {
            showError("Le numéro de téléphone doit être un nombre valide.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visiteur/AfficherVisiteur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nom.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher un message d'erreur
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour afficher un message d'information
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

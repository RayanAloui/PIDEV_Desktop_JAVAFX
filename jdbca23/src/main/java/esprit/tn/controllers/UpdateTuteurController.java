package esprit.tn.controllers;

import esprit.tn.entities.Tuteur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import esprit.tn.services.ServiceTuteur;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateTuteurController {

    @FXML
    private TextField cinID, nomID, prenomID, telephoneID, adresseID, emailID;
    @FXML
    private ComboBox<String> disponibiliteID;

    @FXML
    private Label errorCIN, errorNOM, errorPRENOM, errorTELEPHONE, errorADRESSE, errorEMAIL, errorDISPONIBILITE;

    private Tuteur currentTuteur;
    private ServiceTuteur serviceTuteur = new ServiceTuteur();

    public void initData(Tuteur tuteur) {
        this.currentTuteur = tuteur;
        cinID.setText(tuteur.getCinT());
        nomID.setText(tuteur.getNomT());
        prenomID.setText(tuteur.getPrenomT());
        telephoneID.setText(tuteur.getTelephoneT());
        adresseID.setText(tuteur.getAdresseT());
        emailID.setText(tuteur.getEmail());
        disponibiliteID.getItems().addAll("oui", "non");
        disponibiliteID.setValue(tuteur.getDisponibilite());
    }

    @FXML
    void sauvegarderTuteur(ActionEvent event) {
        resetErrors(); // Efface les erreurs affichées avant validation

        // Vérifier si un tuteur est bien sélectionné pour modification
        if (currentTuteur == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun tuteur sélectionné pour modification !");
            return;
        }

        // Récupération et nettoyage des entrées utilisateur
        String cin = cinID.getText().trim();
        String nom = nomID.getText().trim();
        String prenom = prenomID.getText().trim();
        String telephone = telephoneID.getText().trim();
        String adresse = adresseID.getText().trim();
        String email = emailID.getText().trim();
        String disponibilite = disponibiliteID.getValue();

        // Validation des entrées utilisateur
        boolean isValid = validateInputs(cin, nom, prenom, telephone, disponibilite, email);

        if (isValid) {
            try {
                int idTuteur = currentTuteur.getIdT(); // Récupérer l'ID du tuteur en cours de modification

                // Vérifier si les valeurs existent déjà chez un autre tuteur
                if (serviceTuteur.cinExiste(cin, idTuteur)) {
                    errorCIN.setText("Ce CIN est déjà utilisé !");
                    return;
                }
                if (serviceTuteur.telephoneExiste(telephone, idTuteur)) {
                    errorTELEPHONE.setText("Ce numéro est déjà utilisé !");
                    return;
                }
                if (serviceTuteur.emailExiste(email, idTuteur)) {
                    errorEMAIL.setText("Cet email est déjà utilisé !");
                    return;
                }

                // Mise à jour des informations du tuteur
                currentTuteur.setCinT(cin);
                currentTuteur.setNomT(nom);
                currentTuteur.setPrenomT(prenom);
                currentTuteur.setTelephoneT(telephone);
                currentTuteur.setAdresseT(adresse);
                currentTuteur.setDisponibilite(disponibilite);
                currentTuteur.setEmail(email);

                // Exécuter la mise à jour
                serviceTuteur.updateTuteur(currentTuteur);

                // Afficher un message de succès
                showAlert(Alert.AlertType.INFORMATION, "Mise à jour réussie", "Le tuteur a été mis à jour avec succès !");

                // Redirection vers l'affichage des tuteurs
                redirectToAfficherTuteur(event);

            } catch (SQLException | IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de mise à jour", "Impossible de modifier le tuteur : " + e.getMessage());
            }
        }
    }


    private void resetErrors() {
        errorCIN.setText("");
        errorNOM.setText("");
        errorPRENOM.setText("");
        errorTELEPHONE.setText("");
        errorADRESSE.setText("");
        errorEMAIL.setText("");
        errorDISPONIBILITE.setText("");
    }

    private boolean validateInputs(String cin, String nom, String prenom, String telephone, String disponibilite, String email) {
        boolean isValid = true;

        if (!cin.matches("\\d{8}")) {
            errorCIN.setText("Le CIN doit contenir exactement 8 chiffres !");
            isValid = false;
        }
        if (!nom.matches("[a-zA-ZÀ-ÿ\\s-]+")) {
            errorNOM.setText("Le nom ne doit contenir que des lettres !");
            isValid = false;
        }
        if (!prenom.matches("[a-zA-ZÀ-ÿ\\s-]+")) {
            errorPRENOM.setText("Le prénom ne doit contenir que des lettres !");
            isValid = false;
        }
        if (!telephone.matches("\\d{8}")) {
            errorTELEPHONE.setText("Le téléphone doit contenir exactement 8 chiffres !");
            isValid = false;
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            errorEMAIL.setText("Email invalide !");
            isValid = false;
        }
        if (disponibilite == null) {
            errorDISPONIBILITE.setText("Veuillez choisir une disponibilité !");
            isValid = false;
        }

        return isValid;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void redirectToAfficherTuteur(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherTuteur.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Liste des Tuteurs");
        stage.show();
    }
}





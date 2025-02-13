package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import entities.Tuteur;
import services.ServiceTuteur;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateTuteurController {

    @FXML
    private TextField cinID, nomID, prenomID, telephoneID, adresseID;

    @FXML
    private Label errorCIN, errorNOM, errorPRENOM, errorTELEPHONE, errorADRESSE;

    private Tuteur currentTuteur;
    private ServiceTuteur serviceTuteur = new ServiceTuteur();

    public void initData(Tuteur tuteur) {
        currentTuteur = tuteur;
        cinID.setText(tuteur.getCinT());
        nomID.setText(tuteur.getNomT());
        prenomID.setText(tuteur.getPrenomT());
        telephoneID.setText(tuteur.getTelephoneT());
        adresseID.setText(tuteur.getAdresseT());
    }

    @FXML
    void sauvegarderTuteur(ActionEvent event) {
        // Réinitialiser les erreurs
        errorCIN.setText("");
        errorNOM.setText("");
        errorPRENOM.setText("");
        errorTELEPHONE.setText("");

        String cin = cinID.getText().trim();
        String nom = nomID.getText().trim();
        String prenom = prenomID.getText().trim();
        String telephone = telephoneID.getText().trim();
        String adresse = adresseID.getText().trim();

        boolean isValid = true;

        // Vérification du CIN (exactement 8 chiffres)
        if (!cin.matches("\\d{8}")) {
            errorCIN.setText("Le CIN doit contenir exactement 8 chiffres !");
            isValid = false;
        }

        // Vérification du nom (uniquement des lettres)
        if (!nom.matches("[a-zA-Z]+")) {
            errorNOM.setText("Le nom ne doit contenir que des lettres !");
            isValid = false;
        }

        // Vérification du prénom (uniquement des lettres)
        if (!prenom.matches("[a-zA-Z]+")) {
            errorPRENOM.setText("Le prénom ne doit contenir que des lettres !");
            isValid = false;
        }

        // Vérification du téléphone (exactement 8 chiffres)
        if (!telephone.matches("\\d{8}")) {
            errorTELEPHONE.setText("Le téléphone doit contenir exactement 8 chiffres !");
            isValid = false;
        }

        // Adresse : pas de validation stricte car elle est optionnelle

        if (isValid) {
            try {
                // Mise à jour du tuteur
                currentTuteur.setCinT(cin);
                currentTuteur.setNomT(nom);
                currentTuteur.setPrenomT(prenom);
                currentTuteur.setTelephoneT(telephone);
                currentTuteur.setAdresseT(adresse);

                serviceTuteur.updateTuteur(currentTuteur);

                // Redirection vers AfficherTuteur.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherTuteur.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Liste des Tuteurs");
                stage.show();

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}




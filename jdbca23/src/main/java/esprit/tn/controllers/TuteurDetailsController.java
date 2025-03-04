package esprit.tn.controllers;

import esprit.tn.entities.Tuteur;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TuteurDetailsController {

    @FXML
    private Label lblCin;
    @FXML
    private Label lblNom;
    @FXML
    private Label lblPrenom;
    @FXML
    private Label lblTelephone;
    @FXML
    private Label lblAdresse;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblDisponibilite;

    public void setTuteur(Tuteur tuteur) {
        lblCin.setText("CIN : " + tuteur.getCinT());
        lblNom.setText("Nom : " + tuteur.getNomT());
        lblPrenom.setText("Prénom : " + tuteur.getPrenomT());
        lblTelephone.setText("Téléphone : " + tuteur.getTelephoneT());
        lblAdresse.setText("Adresse : " + tuteur.getAdresseT());
        lblEmail.setText("Email : " + tuteur.getEmail());
        lblDisponibilite.setText("Disponibilité : " + tuteur.getDisponibilite());
    }
}


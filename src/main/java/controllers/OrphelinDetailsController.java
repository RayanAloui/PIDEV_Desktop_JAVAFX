package controllers;

import entities.Orphelin;
import entities.Tuteur;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.ServiceTuteur;

import java.sql.SQLException;

public class OrphelinDetailsController {

    @FXML
    private Label lblNom;
    @FXML
    private Label lblPrenom;
    @FXML
    private Label lblDateNaissance;
    @FXML
    private Label lblSexe;
    @FXML
    private Label lblSituationScolaire;
    @FXML
    private Label lblTuteur;

    public void setOrphelin(Orphelin orphelin) throws SQLException {
        lblNom.setText("Nom : " + orphelin.getNomO());
        lblPrenom.setText("Prénom : " + orphelin.getPrenomO());
        lblDateNaissance.setText("Date de naissance : " + orphelin.getDateNaissance());
        lblSexe.setText("Sexe : " + orphelin.getSexe());
        lblSituationScolaire.setText("Situation scolaire : " + orphelin.getSituationScolaire());
        ServiceTuteur tuteurService = new ServiceTuteur();
        Tuteur tuteur = tuteurService.getTuteurById(orphelin.getIdTuteur());
        if (tuteur != null) {
            lblTuteur.setText(tuteur.getNomT() + " " + tuteur.getPrenomT());
        } else {
            lblTuteur.setText("Tuteur inconnu");
        }

    }
}


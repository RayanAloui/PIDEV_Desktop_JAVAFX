package esprit.tn.controllers;

import esprit.tn.entities.Orphelin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import esprit.tn.services.ServiceOrphelin;
import esprit.tn.services.ServiceTuteur;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class UpdateOrphelinController {

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private DatePicker txtDateNaissance;
    @FXML
    private ComboBox<String> comboSexe;
    @FXML
    private TextField txtSituation;
    @FXML
    private ComboBox<Integer> comboTuteur;
    @FXML
    private Button btnEnregistrer;

    @FXML
    private Label errorNom;
    @FXML
    private Label errorPrenom;
    @FXML
    private Label errorDateNaissance;
    @FXML
    private Label errorSexe;
    @FXML
    private Label errorSituation;
    @FXML
    private Label errorTuteur;

    private final ServiceOrphelin serviceOrphelin = new ServiceOrphelin();
    private final ServiceTuteur serviceTuteur = new ServiceTuteur();
    private Orphelin orphelin;

    @FXML
    public void initialize() {
        // Initialisation du ComboBox Sexe
        comboSexe.getItems().addAll("M", "F");

        // Récupération des IDs de tuteur et ajout dans le ComboBox
        try {
            List<Integer> tuteurIds = serviceTuteur.getAllTuteurIds();
            comboTuteur.getItems().addAll(tuteurIds);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Contrôles de saisie pour les champs de texte
        /*txtNom.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("[a-zA-Zéèêôûùà'\\- ]*")) ? change : null));

        txtPrenom.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("[a-zA-Zéèêôûùà'\\- ]*")) ? change : null));

        txtSituation.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("[a-zA-Zéèêôûùà'\\- ]*")) ? change : null));*/
    }


    public void initData(Orphelin orphelin) {
        this.orphelin = orphelin;
        txtNom.setText(orphelin.getNomO());
        txtPrenom.setText(orphelin.getPrenomO());
        txtDateNaissance.setValue(LocalDate.parse(orphelin.getDateNaissance()));
        comboSexe.setValue(orphelin.getSexe());
        txtSituation.setText(orphelin.getSituationScolaire());
        comboTuteur.setValue(orphelin.getIdTuteur());
    }

    @FXML
    public void enregistrerModifications(ActionEvent event) {
        if (!validerChamps()) {
            return;
        }

        try {
            orphelin.setNomO(txtNom.getText().trim());
            orphelin.setPrenomO(txtPrenom.getText().trim());
            orphelin.setDateNaissance(txtDateNaissance.getValue().toString());
            orphelin.setSexe(comboSexe.getValue());
            orphelin.setSituationScolaire(txtSituation.getText().trim());
            orphelin.setIdTuteur(comboTuteur.getValue());

            serviceOrphelin.updateOrphelin(orphelin);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mise à jour réussie");
            alert.setHeaderText(null);
            alert.setContentText("Les informations de l'orphelin ont été mises à jour.");
            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOrphelin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Orphelins");
            stage.show();

            // Fermeture de la fenêtre actuelle
            ((Stage) btnEnregistrer.getScene().getWindow()).close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    /*private boolean validerChamps() {
        if (txtNom.getText().trim().isEmpty() ||
                txtPrenom.getText().trim().isEmpty() ||
                txtDateNaissance.getValue() == null ||
                comboSexe.getValue() == null ||
                comboTuteur.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs obligatoires");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs obligatoires !");
            alert.showAndWait();
            return false;
        }
        return true;
    }*/

    private boolean validerChamps() {
        boolean valid = true;

        // Réinitialiser les messages d'erreur
        errorNom.setText("");
        errorPrenom.setText("");
        errorDateNaissance.setText("");
        errorSexe.setText("");
        errorSituation.setText("");
        errorTuteur.setText("");

        // Vérification des champs obligatoires
        if (txtNom.getText().trim().isEmpty() ||
                txtPrenom.getText().trim().isEmpty() ||
                txtDateNaissance.getValue() == null ||
                comboSexe.getValue() == null ||
                comboTuteur.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs obligatoires");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs obligatoires !");
            alert.showAndWait();
            valid = false;
        }

        // Contrôle de saisie : vérifier que les champs Nom, Prénom et Situation ne contiennent pas de chiffres
        if (!txtNom.getText().trim().isEmpty() && txtNom.getText().matches(".*\\d.*")) {
            errorNom.setText("Le nom ne doit pas contenir de chiffres.");
            valid = false;
        }
        if (!txtPrenom.getText().trim().isEmpty() && txtPrenom.getText().matches(".*\\d.*")) {
            errorPrenom.setText("Le prénom ne doit pas contenir de chiffres.");
            valid = false;
        }
        if (!txtSituation.getText().trim().isEmpty() && txtSituation.getText().matches(".*\\d.*")) {
            errorSituation.setText("La situation scolaire ne doit pas contenir de chiffres.");
            valid = false;
        }

        return valid;
    }
}





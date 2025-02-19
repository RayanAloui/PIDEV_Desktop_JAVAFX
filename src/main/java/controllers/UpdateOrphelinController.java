package controllers;

import entities.Orphelin;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ServiceOrphelin;
import services.ServiceTuteur;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
    private ComboBox<String> comboSituationScolaire;
    @FXML
    private ComboBox<String> comboTuteur;
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
    private int selectedTuteurId;
    private Map<String, Integer> tuteursMap = new HashMap<>();

    @FXML
    public void initialize() {
        // Initialisation du ComboBox Sexe
        comboSexe.getItems().addAll("M", "F");
        comboSituationScolaire.getItems().addAll("Primaire", "Collège", "Lycée", "Université", "Aucune");

        try {
            // Charger les tuteurs dans un Map (Nom Prénom -> ID)
            Map<Integer, String> fetchedTuteurs = serviceTuteur.getAllTuteurs();

            for (Map.Entry<Integer, String> entry : fetchedTuteurs.entrySet()) {
                tuteursMap.put(entry.getValue(), entry.getKey());
            }

            comboTuteur.setItems(FXCollections.observableArrayList(tuteursMap.keySet()));

            // Gérer la sélection du tuteur
            comboTuteur.setOnAction(event -> {
                String selectedNomPrenom = comboTuteur.getValue();
                selectedTuteurId = tuteursMap.getOrDefault(selectedNomPrenom, -1);
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initData(Orphelin orphelin) {
        this.orphelin = orphelin;
        txtNom.setText(orphelin.getNomO());
        txtPrenom.setText(orphelin.getPrenomO());
        txtDateNaissance.setValue(LocalDate.parse(orphelin.getDateNaissance()));
        comboSexe.setValue(orphelin.getSexe());
        comboSituationScolaire.setValue(orphelin.getSituationScolaire());

        try {
            // Préparer les tuteurs
            Map<Integer, String> fetchedTuteurs = serviceTuteur.getAllTuteurs();
            comboTuteur.setItems(FXCollections.observableArrayList(fetchedTuteurs.values()));

            // Sélectionner le bon tuteur
            if (fetchedTuteurs.containsKey(orphelin.getIdTuteur())) {
                comboTuteur.setValue(fetchedTuteurs.get(orphelin.getIdTuteur()));
                selectedTuteurId = orphelin.getIdTuteur();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            orphelin.setSituationScolaire(comboSituationScolaire.getValue());
            orphelin.setIdTuteur(selectedTuteurId);

            serviceOrphelin.updateOrphelin(orphelin);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mise à jour réussie");
            alert.setHeaderText(null);
            alert.setContentText("Les informations de l'orphelin ont été mises à jour.");
            alert.showAndWait();

            // Revenir à la liste des orphelins
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOrphelin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Orphelins");
            stage.show();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validerChamps() {
        boolean valid = true;

        // Réinitialisation des messages d'erreur
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
            return false;
        }

        // Vérification du format des champs texte (pas de chiffres)
        if (txtNom.getText().matches(".*\\d.*")) {
            errorNom.setText("Le nom ne doit pas contenir de chiffres.");
            valid = false;
        }
        if (txtPrenom.getText().matches(".*\\d.*")) {
            errorPrenom.setText("Le prénom ne doit pas contenir de chiffres.");
            valid = false;
        }

        // Vérification de la date de naissance (ne doit pas être future)
        if (txtDateNaissance.getValue().isAfter(LocalDate.now())) {
            errorDateNaissance.setText("La date de naissance ne peut pas être dans le futur.");
            valid = false;
        }

        return valid;
    }
}

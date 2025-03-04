package esprit.tn.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import esprit.tn.entities.Activite;
import esprit.tn.services.ActiviteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AfficherActiviteController {

    @FXML
    private TableView<Activite> tableViewActivites;
    @FXML
    private TableColumn<Activite, Integer> columnId;
    @FXML
    private TableColumn<Activite, String> columnNom;
    @FXML
    private TableColumn<Activite, String> columnCategorie;
    @FXML
    private TableColumn<Activite, String> columnDuree;
    @FXML
    private TableColumn<Activite, String> columnNomTuteur;
    @FXML
    private TableColumn<Activite, String> columnDateActivite;
    @FXML
    private TableColumn<Activite, String> columnLieu;
    @FXML
    private TableColumn<Activite, String> columnDescription;
    @FXML
    private TableColumn<Activite, String> columnStatut;

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> comboBoxCategorie;
    @FXML
    private PieChart pieChart;

    private ObservableList<Activite> activitesList = FXCollections.observableArrayList();
    private final ActiviteService activiteService = new ActiviteService();

    // Méthode pour initialiser le tableau
    public void initialize() {
        // Configuration des colonnes de la TableView
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        columnCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        columnDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        columnNomTuteur.setCellValueFactory(new PropertyValueFactory<>("nom_du_tuteur"));
        columnDateActivite.setCellValueFactory(new PropertyValueFactory<>("date_activite"));
        columnLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Charger les activités depuis le service
        loadActivities();


    }

    private void loadActivities() {
        List<Activite> activites = activiteService.getall(); // Méthode pour récupérer les activités
        activitesList.setAll(activites);
        tableViewActivites.setItems(activitesList);
    }


    @FXML
    public void generatePdf(ActionEvent event) {
        if (activitesList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucune activité", "La liste des activités est vide !");
            return;
        }

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("activites.pdf"));
            document.open();
            document.add(new Paragraph("Liste des Activités\n\n"));

            for (Activite activite : activitesList) {
                document.add(new Paragraph("ID: " + activite.getId()));
                document.add(new Paragraph("Nom: " + activite.getNom()));
                document.add(new Paragraph("Catégorie: " + activite.getCategorie()));
                document.add(new Paragraph("Durée: " + activite.getDuree()));
                document.add(new Paragraph("Tuteur: " + activite.getNom_du_tuteur()));
                document.add(new Paragraph("Date: " + activite.getDate_activite()));
                document.add(new Paragraph("Lieu: " + activite.getLieu()));
                document.add(new Paragraph("Description: " + activite.getDescription()));
                document.add(new Paragraph("Statut: " + activite.getStatut()));
                document.add(new Paragraph("----------------------------------------------------"));
            }

            document.close();
            showAlert(Alert.AlertType.INFORMATION, "PDF Généré", "Le fichier 'activites.pdf' a été créé avec succès !");
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Optional<ButtonType> showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }



    @FXML
    public void DeleteActivite(ActionEvent event) {
        Activite selectedActivite = tableViewActivites.getSelectionModel().getSelectedItem();
        if (selectedActivite != null) {
            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Suppression", "Voulez-vous supprimer cette activité ?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                activitesList.remove(selectedActivite);
                tableViewActivites.refresh();

            }
        }
    }

    @FXML
    public void GoToAddActivite(ActionEvent actionEvent) {
        navigateTo("/AjouterActivite.fxml");
    }
    @FXML
    public void GoToUpdateActivite(ActionEvent actionEvent) {
        UpdateActiviteController updateActiviteController = new UpdateActiviteController();
        Activite a = tableViewActivites.getSelectionModel().getSelectedItem();

        updateActiviteController.SetActivite(a);
        navigateTo("/UpdateActivite.fxml");
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) tableViewActivites.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page demandée.");
            e.printStackTrace();
        }
    }

    @FXML
    public void GoToParticipant(ActionEvent actionEvent) {
        navigateTo("/AfficherParticipant.fxml");
    }
}


package esprit.tn.controllers;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import esprit.tn.entities.visiteurs;
import esprit.tn.services.VisiteursService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import esprit.tn.entities.visites;
import esprit.tn.services.VisitesService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AfficherVisite {

    @FXML
    private TableView<visites> TableView;

    @FXML
    private TableColumn<visites, String> date;

    @FXML
    private TableColumn<visites, String> heure;

    @FXML
    private TableColumn<visites, String> motif;

    @FXML
    private TableColumn<visites, String> statut;

    public int IDV;

    @FXML
    void initialize(int visiteurId) {
        VisitesService vs = new VisitesService();
        ObservableList<visites> observableList = FXCollections.observableList(vs.getVisitesByVisiteur(visiteurId));
        TableView.setItems(observableList);

        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        heure.setCellValueFactory(new PropertyValueFactory<>("heure"));
        motif.setCellValueFactory(new PropertyValueFactory<>("motif"));
        statut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Permettre le tri automatique des colonnes
        date.setSortable(true);
        heure.setSortable(true);

        // Ajouter un écouteur d'événements pour trier par date
        date.setSortType(TableColumn.SortType.ASCENDING);
        date.setOnEditStart(event -> toggleSort(date));

        // Ajouter un écouteur d'événements pour trier par heure
        heure.setSortType(TableColumn.SortType.ASCENDING);
        heure.setOnEditStart(event -> toggleSort(heure));
    }

    private void toggleSort(TableColumn<visites, ?> column) {
        if (column.getSortType() == TableColumn.SortType.ASCENDING) {
            column.setSortType(TableColumn.SortType.DESCENDING);
        } else {
            column.setSortType(TableColumn.SortType.ASCENDING);
        }
        TableView.getSortOrder().clear();
        TableView.getSortOrder().add(column);
    }


    @FXML
    void ajouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visite/AjouterVisite.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) TableView.getScene().getWindow();
            AjouterVisite controller = loader.getController();
            controller.initData(IDV);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void modifier(ActionEvent event) {
        // Vérifier si un visiteur est sélectionné dans la TableView
        visites visiteSelectionne = TableView.getSelectionModel().getSelectedItem();

        if (visiteSelectionne == null) {
            // Si aucun visiteur n'est sélectionné, afficher une alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un visiteur à modifier !");
            alert.showAndWait();
            return;
        }

        try {
            // Si un visiteur est sélectionné, on passe ses informations à la page de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visite/ModifierVisite.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la page ModifierVisiteur
            ModifierVisite controller = loader.getController();
            controller.setVisite(visiteSelectionne);  // Passe le visiteur sélectionné au contrôleur de la page de modification

            // Changer la scène pour afficher la page de modification
            Stage stage = (Stage) TableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void supprimer(ActionEvent event) {
        // Vérifier si une ligne est sélectionnée
        visites visiteSelectionne = TableView.getSelectionModel().getSelectedItem();

        if (visiteSelectionne == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un visiteur à supprimer !");
            alert.showAndWait();
            return;
        }

        // Confirmation de suppression
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Suppression du visiteur");
        confirmation.setContentText("Voulez-vous vraiment supprimer le RDV a la date: " + visiteSelectionne.getDate() +" et a l'heure:"+ visiteSelectionne.getHeure() + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer de la base de données
            VisitesService vs = new VisitesService();
            vs.supprimer(visiteSelectionne.getId());

            // Supprimer de la TableView
            TableView.getItems().remove(visiteSelectionne);

            // Afficher une alerte de succès
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Suppression réussie");
            success.setHeaderText(null);
            success.setContentText("Le visiteur a été supprimé avec succès !");
            success.showAndWait();
        }
    }

    @FXML
    public void menu(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();


            stage.setScene(new Scene(root));


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the users page");
            alert.setContentText("An error occurred while trying to navigate back to the user list.");
            alert.showAndWait();
        }
    }

    @FXML
    void PDF(ActionEvent event) {
        // Récupérer les informations du visiteur
        VisiteursService visiteurService = new VisiteursService();
        visiteurs visiteur = visiteurService.getone(IDV);  // Assure-toi d'avoir cette méthode

        if (visiteur == null) {
            showAlert("Erreur", "Impossible de récupérer les informations du visiteur.");
            return;
        }

        // Choisir un emplacement pour enregistrer le fichier PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(TableView.getScene().getWindow());

        if (file == null) return; // L'utilisateur a annulé l'enregistrement

        try {
            // Création du document PDF
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Titre du document
            document.add(new Paragraph("Détails du Visiteur").setBold().setFontSize(16));

            // Informations du visiteur
            document.add(new Paragraph("Nom: " + visiteur.getNom()));
            document.add(new Paragraph("Prénom: " + visiteur.getPrenom()));
            document.add(new Paragraph("Email: " + visiteur.getEmail()));
            document.add(new Paragraph("Téléphone: " + visiteur.getTel()));
            document.add(new Paragraph("Adresse: " + visiteur.getAdresse()));
            document.add(new Paragraph("CIN: " + visiteur.getCin()));
            document.add(new Paragraph("\n")); // Saut de ligne

            // Liste des visites
            document.add(new Paragraph("Liste des Visites").setBold().setFontSize(14));

            // Création du tableau
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 3, 2})).useAllAvailableWidth();
            table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Heure").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Motif").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Statut").setBold()));

            // Récupération des visites
            VisitesService visitesService = new VisitesService();
            List<visites> listeVisites = visitesService.getVisitesByVisiteur(IDV);

            for (visites v : listeVisites) {
                table.addCell(new Cell().add(new Paragraph(v.getDate().toString())));
                table.addCell(new Cell().add(new Paragraph(v.getHeure().toString())));
                table.addCell(new Cell().add(new Paragraph(v.getMotif())));
                table.addCell(new Cell().add(new Paragraph(v.getStatut())));
            }

            document.add(table);
            document.close();

            showAlert("Succès", "PDF généré avec succès !");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de générer le PDF.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

package esprit.tn.controllers;

import esprit.tn.entities.Participant;
import esprit.tn.services.ParticipantService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AfficherParticipantController {

    @FXML
    private TableView<Participant> tableViewParticipants;

    @FXML
    private TableColumn<Participant, Integer> columnId;

    @FXML
    private TableColumn<Participant, String> columnNom;

    @FXML
    private TableColumn<Participant, String> columnPrenom;

    @FXML
    private TableColumn<Participant, Integer> columnAge;

    @FXML
    private TextField searchField; // Champ de recherche

    @FXML
    private PieChart agePieChart; // PieChart pour les statistiques d'âge

    private static Participant selectedParticipant;

    private ParticipantService participantService = new ParticipantService();

    public void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        columnPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        columnAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        loadParticipants();
        displayAgeStatistics(); // Afficher les statistiques d'âge dès le départ
    }

    private void loadParticipants() {
        List<Participant> participants = participantService.getall();
        ObservableList<Participant> observableList = FXCollections.observableArrayList(participants);
        tableViewParticipants.setItems(observableList);
    }

    @FXML
    private void GoToAddParticipant() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterParticipant.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableViewParticipants.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading AjouterParticipant.fxml");
        }
    }

    @FXML
    private void DeleteParticipant() {
        Participant selectedParticipant = tableViewParticipants.getSelectionModel().getSelectedItem();
        if (selectedParticipant != null) {
            participantService.supprimer(selectedParticipant.getId());
            tableViewParticipants.getItems().remove(selectedParticipant);
            System.out.println("Participant supprimé avec succès !");
        } else {
            System.out.println("Veuillez sélectionner un participant à supprimer.");
        }
    }

    @FXML
    private void GoToUpdateParticipant() {
        selectedParticipant = tableViewParticipants.getSelectionModel().getSelectedItem();
        if (selectedParticipant != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateParticipant.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) tableViewParticipants.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error loading UpdateParticipant.fxml");
            }
        } else {
            System.out.println("Veuillez sélectionner un participant à modifier.");
        }
    }

    public static Participant getSelectedParticipant() {
        return selectedParticipant;
    }

    public static void setSelectedParticipant(Participant participant) {
        selectedParticipant = participant;
    }

    @FXML
    private void searchParticipant() {
        String searchTerm = searchField.getText().toLowerCase();
        List<Participant> participants = participantService.getall();
        List<Participant> filteredParticipants = participants.stream()
                .filter(participant -> participant.getNom().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());

        ObservableList<Participant> observableList = FXCollections.observableArrayList(filteredParticipants);
        tableViewParticipants.setItems(observableList);
    }

    private void displayAgeStatistics() {
        List<Participant> participants = participantService.getall();

        // Count participants in each age range
        long under15 = participants.stream().filter(p -> p.getAge() < 15).count();
        long between15And25 = participants.stream().filter(p -> p.getAge() >= 15 && p.getAge() <= 25).count();
        long over25 = participants.stream().filter(p -> p.getAge() > 25).count();

        // Create pie chart data
        PieChart.Data slice1 = new PieChart.Data("Under 15", under15);
        PieChart.Data slice2 = new PieChart.Data("15 to 25", between15And25);
        PieChart.Data slice3 = new PieChart.Data("Over 25", over25);

        // Set the chart data
        agePieChart.getData().clear();
        agePieChart.getData().addAll(slice1, slice2, slice3);
    }

    @FXML
    public void GoToActivite(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherActivite.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading AfficherActivite.fxml");
        }
    }

    @FXML
    private void exportToPDF() {
        // Create a file chooser to specify the location and name of the file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                // Create document and writer
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));

                // Open the document
                document.open();

                // Add title to the document
                document.add(new Paragraph("Participants List"));
                document.add(new Paragraph(" "));

                // Create a table for the participants
                PdfPTable pdfTable = new PdfPTable(4); // 4 columns: ID, Nom, Prenom, Age
                pdfTable.addCell("ID");
                pdfTable.addCell("Nom");
                pdfTable.addCell("Prénom");
                pdfTable.addCell("Âge");

                // Populate the table with participant data
                List<Participant> participants = participantService.getall();
                for (Participant participant : participants) {
                    pdfTable.addCell(String.valueOf(participant.getId()));
                    pdfTable.addCell(participant.getNom());
                    pdfTable.addCell(participant.getPrenom());
                    pdfTable.addCell(String.valueOf(participant.getAge()));
                }

                // Add the table to the document
                document.add(pdfTable);

                // Close the document
                document.close();

                System.out.println("PDF exported successfully!");

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error exporting to PDF");
            }
        }
    }

    public void back(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
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
}





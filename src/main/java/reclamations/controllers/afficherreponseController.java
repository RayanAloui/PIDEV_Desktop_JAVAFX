package reclamations.controllers;

import reclamations.services.ReponseService;
import reclamations.entities.Reponse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.control.cell.PropertyValueFactory;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import java.io.IOException;
import java.io.File;

public class afficherreponseController {

    @FXML
    private TableView<Reponse> tableViewReponses;

    @FXML
    private TableColumn<Reponse, Integer> columnId;

    @FXML
    private TableColumn<Reponse, String> columndescription;

    @FXML
    private TableColumn<Reponse, String> columndate;

    @FXML
    private TableColumn<Reponse, String> columnstatut;

    @FXML
    public void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columndescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columndate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnstatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        loadReponses();
    }

    private void loadReponses() {
        ReponseService reponseService = new ReponseService();
        ObservableList<Reponse> reponses = FXCollections.observableArrayList(reponseService.getall());
        tableViewReponses.setItems(reponses);
    }

    @FXML
    void GoToAddreponse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterreponse.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to load add response page", e.getMessage());
        }
    }

    @FXML
    void Deletereponse(ActionEvent event) {
        Reponse selectedReponse = tableViewReponses.getSelectionModel().getSelectedItem();

        if (selectedReponse != null) {
            ReponseService reponseService = new ReponseService();
            reponseService.supprimer(selectedReponse.getId());
            loadReponses();
            showInfoAlert("Response deleted successfully", null);
        } else {
            showWarningAlert("No response selected", "Please select a response to delete.");
        }
    }

    @FXML
    void GoToUpdatereponse(ActionEvent event) {
        Reponse selectedReponse = tableViewReponses.getSelectionModel().getSelectedItem();

        if (selectedReponse != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatereponse.fxml"));
                Parent root = loader.load();
                updatereponseController controller = loader.getController();
                controller.setReponseData(selectedReponse);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showErrorAlert("Failed to load update response page", e.getMessage());
            }
        } else {
            showWarningAlert("No Response Selected", "Please select a response to update.");
        }
    }

    @FXML
    void pdf(ActionEvent event) {
        Reponse selectedReponse = tableViewReponses.getSelectionModel().getSelectedItem();

        if (selectedReponse == null) {
            showWarningAlert("No selection", "Please select a response to export to PDF!");
            return;
        }

        // File chooser for saving the PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Response_" + selectedReponse.getId() + ".pdf");
        File file = fileChooser.showSaveDialog(null);

        if (file == null) {
            return;
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Colors
            PDColor blue = new PDColor(new float[]{0, 0, 1}, PDDeviceRGB.INSTANCE);
            PDColor gray = new PDColor(new float[]{0.9f, 0.9f, 0.9f}, PDDeviceRGB.INSTANCE);
            PDColor black = new PDColor(new float[]{0, 0, 0}, PDDeviceRGB.INSTANCE);

            // Header background
            contentStream.setNonStrokingColor(blue);
            contentStream.addRect(50, 720, 500, 50);
            contentStream.fill();

            // Title
            contentStream.setNonStrokingColor(black);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.newLineAtOffset(220, 740);
            contentStream.showText("Response Details");
            contentStream.endText();

            // Table configuration
            float startX = 50;
            float startY = 680;
            float rowHeight = 30;
            float tableWidth = 500;
            float colWidth = tableWidth / 2; // Two columns: field/value

            // Data to display (Fixed the incorrect initializer)
            String[][] data = {
                    {"ID", String.valueOf(selectedReponse.getId())},
                    {"Date", selectedReponse.getDate().toString()}, // Fixed this line
                    {"Description", selectedReponse.getDescription()},
                    {"Status", selectedReponse.getStatut()}
            };

            // Write rows
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            float textY = startY - 15;
            boolean isGray = true;

            for (String[] row : data) {
                if (isGray) {
                    contentStream.setNonStrokingColor(gray);
                    contentStream.addRect(startX, textY - 10, tableWidth, rowHeight);
                    contentStream.fill();
                }

                contentStream.setNonStrokingColor(black);

                // Field (Column 1)
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(startX + 10, textY);
                contentStream.showText(row[0] + ":");
                contentStream.endText();

                // Value (Column 2)
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(startX + colWidth + 10, textY);
                contentStream.showText(row[1]);
                contentStream.endText();

                textY -= rowHeight;
                isGray = !isGray;
            }

            // Final message
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.newLineAtOffset(200, textY - 40);
            contentStream.showText("Thank you for your support!");
            contentStream.endText();

            // Close the content stream
            contentStream.close();
            document.save(file);

            // Success message
            showInfoAlert("PDF Exported", "PDF file has been created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Could not create PDF file", e.getMessage());
        }
    }


    private void showErrorAlert(String headerText, String contentText) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText(headerText);
        error.setContentText(contentText);
        error.showAndWait();
    }

    private void showInfoAlert(String headerText, String contentText) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Information");
        info.setHeaderText(headerText);
        info.setContentText(contentText);
        info.showAndWait();
    }

    private void showWarningAlert(String headerText, String contentText) {
        Alert warning = new Alert(Alert.AlertType.WARNING);
        warning.setTitle("Warning");
        warning.setHeaderText(headerText);
        warning.setContentText(contentText);
        warning.showAndWait();
    }
}

package esprit.tn.controllers;

import esprit.tn.services.ITuteurService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import esprit.tn.entities.Reponse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private TableColumn<Reponse, Integer> columnindice;
    @FXML
    private TextField chercherReponse;

    @FXML
    public void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columndescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columndate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnstatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        columnindice.setCellValueFactory(new PropertyValueFactory<>("indice"));


        loadReponses();
    }

    private void loadReponses() {
        ITuteurService.ReponseService reponseService = new ITuteurService.ReponseService();
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
            ITuteurService.ReponseService reponseService = new ITuteurService.ReponseService();
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
    @FXML
    void rechercherReponse() {
        chercherReponse.textProperty().addListener((observable, oldValue, newValue) -> {
            ITuteurService.ReponseService rs = new ITuteurService.ReponseService();
            ObservableList<Reponse> allReponses = FXCollections.observableArrayList(rs.getall());
            ObservableList<Reponse> filteredList = FXCollections.observableArrayList();

            if (newValue == null || newValue.trim().isEmpty()) {
                filteredList.addAll(allReponses);
            } else {
                String lowerCaseFilter = newValue.toLowerCase();
                for (Reponse r : allReponses) {
                    if (r.getDescription().toLowerCase().contains(lowerCaseFilter) ||
                            r.getStatut().toLowerCase().contains(lowerCaseFilter) ||
                            r.getDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                        filteredList.add(r);
                    }
                }
            }
            tableViewReponses.setItems(filteredList);
        });
    }
    @FXML
    void chart(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Statistiques des Réponses - BarChart");

        // Define the axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Statut de Réponse");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre de Réponses");

        // Create the BarChart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Réponses par Statut");

        try {
            ITuteurService.ReponseService reponseService = new ITuteurService.ReponseService();
            List<Reponse> listeReponses = reponseService.getall();

            // Map to count responses based on their statut
            Map<String, Integer> reponseCount = new HashMap<>();

            // Count responses by statut ("traitee" vs "non traitee")
            for (Reponse reponse : listeReponses) {
                String statut = reponse.getStatut(); // "traitee" or "non traitee"
                reponseCount.put(statut, reponseCount.getOrDefault(statut, 0) + 1);
            }

            // Adding data for "Traitee" and "Non Traitee"
            for (Map.Entry<String, Integer> entry : reponseCount.entrySet()) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
                series.getData().add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        barChart.getData().add(series);

        VBox vbox = new VBox(barChart);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 600, 400);
        stage.setScene(scene);
        stage.show();
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

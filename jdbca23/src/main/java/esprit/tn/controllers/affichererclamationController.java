package esprit.tn.controllers;

import esprit.tn.services.ITuteurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import esprit.tn.entities.Reclamation;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;

public class affichererclamationController {

    @FXML
    private TableView<Reclamation> tableViewReclamations;

    @FXML
    private TableColumn<Reclamation, Integer> columnId;

    @FXML
    private TableColumn<Reclamation, String> colummail;

    @FXML
    private TableColumn<Reclamation, String> columndescription;

    @FXML
    private TableColumn<Reclamation, String> columndate;

    @FXML
    private TableColumn<Reclamation, String> columntypereclamation;

    @FXML
    private TextField chercherReclamation;

    @FXML
    public void initialize() {
        // Initialize TableColumn bindings
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colummail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        columndescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columndate.setCellValueFactory(new PropertyValueFactory<>("date"));  // Added date column binding
        columntypereclamation.setCellValueFactory(new PropertyValueFactory<>("typereclamation"));

        // Load data into the TableView
        loadReclamations();
    }

    private void loadReclamations() {
        ITuteurService.ReclamationService reclamationService = new ITuteurService.ReclamationService();
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList(reclamationService.getall());
        tableViewReclamations.setItems(reclamations);
    }

    @FXML
    void GoToAddreclamation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterreclamations.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to load add reclamation page", e.getMessage());
        }
    }

    @FXML
    void Deletererclamation(ActionEvent event) {
        Reclamation selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            ITuteurService.ReclamationService reclamationService = new ITuteurService.ReclamationService();
            reclamationService.supprimer(selectedReclamation.getId());
            loadReclamations();
            showSuccessAlert("Reclamation deleted successfully");
        } else {
            showWarningAlert("No reclamation selected", "Please select a reclamation to delete.");
        }
    }

    @FXML
    void GoToUpdatereclamation(ActionEvent event) {
        Reclamation selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatereclamation.fxml"));
                Parent root = loader.load();
                updaterclamationController controller = loader.getController();
                controller.setReclamationData(selectedReclamation);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showErrorAlert("Failed to load update reclamation page", e.getMessage());
            }
        } else {
            showWarningAlert("No Reclamation Selected", "Please select a reclamation to update.");
        }
    }

    @FXML
    void rechercherReclamation() {
        chercherReclamation.textProperty().addListener((observable, oldValue, newValue) -> {
            ITuteurService.ReclamationService rs = new ITuteurService.ReclamationService();
            ObservableList<Reclamation> allReclamations = FXCollections.observableArrayList(rs.getall());
            ObservableList<Reclamation> filteredList = FXCollections.observableArrayList();
            if (newValue == null || newValue.trim().isEmpty()) {
                filteredList.addAll(allReclamations);
            } else {
                String lowerCaseFilter = newValue.toLowerCase();
                for (Reclamation r : allReclamations) {
                    if (r.getMail().toLowerCase().contains(lowerCaseFilter) ||
                            r.getDescription().toLowerCase().contains(lowerCaseFilter) ||
                            r.getTypereclamation().toLowerCase().contains(lowerCaseFilter)) {
                        filteredList.add(r);
                    }
                }
            }
            tableViewReclamations.setItems(filteredList);
        });
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void GoToajouterreponse(ActionEvent event) {
        Reclamation selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            try {
                // Load the FXML file for updating a reclamation
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterreponse.fxml"));
                Parent root = loader.load();

                AddReponseController controller = loader.getController();
                controller.getreclamationid(selectedReclamation.getId());
                // Get the current stage
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Set the new scene
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();

                // Show an error alert if the page fails to load
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to responde ");
                alert.setContentText("An error occurred while loading the page: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Show a warning if no reclamation is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Reclamation Selected");
            alert.setHeaderText("Please select a reclamation to update");
            alert.showAndWait();
        }

    }

    @FXML
    void chart(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Statistiques des Réclamations - BarChart");

        // Définition des axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Type de Réclamation");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre de Réclamations");

        // Création du BarChart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nombre de Réclamations par Type");

        try {
            ITuteurService.ReclamationService reclamationService = new ITuteurService.ReclamationService();
            List<Reclamation> listeReclamations = reclamationService.getall();

            Map<String, Integer> reclamationCount = new HashMap<>();

            // Compter le nombre de réclamations par type
            for (Reclamation reclamation : listeReclamations) {
                String type = reclamation.getTypereclamation();
                reclamationCount.put(type, reclamationCount.getOrDefault(type, 0) + 1);
            }

            int colorIndex = 0;
            String[] colors = {"#ff5733", "#33ff57", "#3357ff", "#ff33a1", "#ffff33", "#33ffff", "#ff9933"}; // Couleurs

            for (Map.Entry<String, Integer> entry : reclamationCount.entrySet()) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
                series.getData().add(data);

                // Appliquer une couleur unique à chaque barre
                String color = colors[colorIndex % colors.length];
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        newNode.setStyle("-fx-bar-fill: " + color + ";");
                    }
                });

                colorIndex++;
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

    @FXML
    void pdf(ActionEvent event) {
        Reclamation selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();

        if (selectedReclamation == null) {
            showWarningAlert("No selection", "Please select a reclamation to export to PDF!");
            return;
        }

        // File chooser for saving the PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Reclamation_" + selectedReclamation.getId() + ".pdf");
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
            contentStream.showText("Reclamation Details");
            contentStream.endText();

            // Table configuration
            float startX = 50;
            float startY = 680;
            float rowHeight = 30;
            float tableWidth = 500;
            float colWidth = tableWidth / 2; // Two columns: field/value

            // Data to display for Reclamation
            String[][] data = {
                    {"ID", String.valueOf(selectedReclamation.getId())},
                    {"Mail", selectedReclamation.getMail()},
                    {"Description", selectedReclamation.getDescription()},
                    {"Date", selectedReclamation.getDate().toString()},
                    {"Type", selectedReclamation.getTypereclamation()},
                    {"Image Path", selectedReclamation.getImagePath()}
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
            contentStream.showText("Thank you for your attention!");
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


    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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


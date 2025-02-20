package Donateur.tn.controllers;
import Donateur.tn.entities.Dons;
import Donateur.tn.entities.donateur;
import Donateur.tn.services.DonateurService;
import Donateur.tn.services.DonsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class AfficherDonsController {

    @FXML
    private TableView<Dons> TableDons;

    @FXML
    private TableColumn<Dons, Date> dateDon;

    @FXML
    private TableColumn<Dons, String> description;

    @FXML
    private TableColumn<Dons, Double> montant;

    @FXML
    private TableColumn<Dons, String> statut;

    @FXML
    private TableColumn<Dons, String> typeDon;

    @FXML
    private TableColumn<Dons, Integer> donateurId; // Ajout de la colonne donateurId

    @FXML
    void initialize() {
        try {
            DonsService ds = new DonsService();
            ObservableList<Dons> observableList = FXCollections.observableList(ds.getall());
            TableDons.setItems(observableList);

            // Correction des noms des propriétés
            dateDon.setCellValueFactory(new PropertyValueFactory<>("dateDon"));
            System.out.println("dateDon = " + dateDon);
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            montant.setCellValueFactory(new PropertyValueFactory<>("montant"));
            statut.setCellValueFactory(new PropertyValueFactory<>("statut"));
            typeDon.setCellValueFactory(new PropertyValueFactory<>("typeDon"));
            System.out.println("typeDon = " + typeDon);
            donateurId.setCellValueFactory(new PropertyValueFactory<>("donateurId")); // Ajout de la colonne donateurId
        } catch (Exception e) {
            e.printStackTrace();
            // Afficher une alerte à l'utilisateur en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des dons");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void AjouterD(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AjouterDon.fxml")));
            TableDons.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ModifierD(ActionEvent event) {
        // Vérifier si un donnateur est sélectionné dans la TableDonateur
        Dons donSelectionne = TableDons.getSelectionModel().getSelectedItem();

        if (donSelectionne == null) {
            // Si aucun donateur n'est sélectionné, afficher une alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un donateur à modifier !");
            alert.showAndWait();
            return;
        } else {

            try {
                // Si un donateur est sélectionné, on passe ses informations à la page de modification
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierDon.fxml"));
                Parent root = loader.load();

                // Récupérer le contrôleur de la page ModifierVisiteur
                ModifierDonController controller = loader.getController();
                controller.initDataDon(donSelectionne);  // Passe le donateur sélectionné au contrôleur de la page de modification

                // Changer la scène pour afficher la page de modification
                Stage stage = (Stage) TableDons.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    void supprimerD(ActionEvent event) {
        // Implémentez cette fonction
        // Vérifier si un donateur est sélectionné dans la TableDonateur
        Dons donSelectionne = TableDons.getSelectionModel().getSelectedItem();

        if (donSelectionne == null) {
            // Si aucun donateur n'est sélectionné, afficher une alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un donateur à supprimer!");
            alert.showAndWait();
            return;
        }

        // Demander confirmation avant la suppression
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Suppression du don");
        confirmation.setContentText("Voulez-vous vraiment supprimer ce  " + donSelectionne.getTypeDon() + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer de la base de données
            DonsService ds = new DonsService();
            ds.supprimer(donSelectionne.getId());

            // Supprimer de la TableView
            TableDons.getItems().remove(donSelectionne);

            // Afficher une alerte de succès
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Suppression réussie");
            success.setHeaderText(null);
            success.setContentText("Le donateur a été supprimé avec succès !");
            success.showAndWait();
        }
    }

    @FXML
    void pdf(ActionEvent event) {
        Dons donSelectionne = TableDons.getSelectionModel().getSelectedItem();

        if (donSelectionne == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un don pour exporter en PDF !");
            alert.showAndWait();
            return;
        }

        // Boîte de dialogue pour sauvegarder le fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
        fileChooser.setInitialFileName("Don_" + donSelectionne.getId() + ".pdf");
        File file = fileChooser.showSaveDialog(null);

        if (file == null) {
            return;
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // 🟦 Définition des couleurs
            PDColor blue = new PDColor(new float[]{0, 0, 1}, PDDeviceRGB.INSTANCE); // Bleu
            PDColor gray = new PDColor(new float[]{0.9f, 0.9f, 0.9f}, PDDeviceRGB.INSTANCE); // Gris clair
            PDColor black = new PDColor(new float[]{0, 0, 0}, PDDeviceRGB.INSTANCE); // Noir

            // 🎨 Fond coloré pour l'en-tête
            contentStream.setNonStrokingColor(blue);
            contentStream.addRect(50, 720, 500, 50);
            contentStream.fill();

            // ✍️ Titre centré
            contentStream.setNonStrokingColor(black);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.newLineAtOffset(220, 740);
            contentStream.showText("Détails du Don");
            contentStream.endText();

            // 📍 Positionnement de départ
            float startX = 50;
            float startY = 680;
            float rowHeight = 30;
            float tableWidth = 500;
            float colWidth = tableWidth / 2; // Deux colonnes : champ / valeur

            // 🏷️ Données à afficher
            String[][] data = {
                    {"ID du Don", String.valueOf(donSelectionne.getId())},
                    {"Date du Don", String.valueOf(donSelectionne.getDateDon())},
                    {"Type de Don", donSelectionne.getTypeDon()},
                    {"Montant", donSelectionne.getMontant() + " TND"},
                    {"Statut", donSelectionne.getStatut()},
                    {"Description", donSelectionne.getDescription()}
            };

            // 📄 Affichage des lignes avec couleurs
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            float textY = startY - 15;
            boolean isGray = true;

            for (String[] row : data) {
                // 🟢 Alternance des couleurs de fond
                if (isGray) {
                    contentStream.setNonStrokingColor(gray);
                    contentStream.addRect(startX, textY - 10, tableWidth, rowHeight);
                    contentStream.fill();
                }
                contentStream.setNonStrokingColor(black);

                // 🔹 Champ (Colonne 1)
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(startX + 10, textY);
                contentStream.showText(row[0] + ":");
                contentStream.endText();

                // 🔹 Valeur (Colonne 2)
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(startX + colWidth + 10, textY);
                contentStream.showText(row[1]);
                contentStream.endText();

                textY -= rowHeight;
                isGray = !isGray;
            }

            // 💙 Message final
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.newLineAtOffset(200, textY - 40);
            contentStream.showText("Merci pour votre générosité !");
            contentStream.endText();

            // 🏁 Fermeture
            contentStream.close();
            document.save(file);

            // ✅ Message de succès
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("PDF exporté");
            success.setHeaderText(null);
            success.setContentText("Le fichier PDF a été créé avec succès !");
            success.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Erreur");
            error.setHeaderText("Impossible de créer le fichier PDF");
            error.setContentText(e.getMessage());
            error.showAndWait();
        }

    }

    @FXML
    void chart(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Statistiques des Dons - BarChart");

        // Définition des axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date du Don");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Montant (TND)");

        // Création du BarChart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Montant des Dons par Date");

        try {
            DonsService donsService = new DonsService();
            List<Dons> listeDons = donsService.getall();

            Map<String, Double> montantParDate = new HashMap<>();

            // Agréger les montants par date
            for (Dons don : listeDons) {
                String dateString = don.getDateDon().toString(); // Convertir Date en String
                montantParDate.put(dateString, montantParDate.getOrDefault(dateString, 0.0) + don.getMontant());
            }

            int colorIndex = 0;
            String[] colors = {"#ff5733", "#33ff57", "#3357ff", "#ff33a1", "#ffff33", "#33ffff", "#ff9933"}; // Couleurs

            for (Map.Entry<String, Double> entry : montantParDate.entrySet()) {
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
}


package controllers;

import entities.Tuteur;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import services.ServiceTuteur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.stage.FileChooser;


public class AfficherTuteurController {

    @FXML
    private TableView<Tuteur> tableTuteurs;

    @FXML
    private TableColumn<Tuteur, String> colId;

    @FXML
    private TableColumn<Tuteur, String> colCin;

    @FXML
    private TableColumn<Tuteur, String> colNom;

    @FXML
    private TableColumn<Tuteur, String> colPrenom;

    @FXML
    private TableColumn<Tuteur, String> colTelephone;

    @FXML
    private TableColumn<Tuteur, String> colAdresse;

    @FXML
    private TextField searchTuteur;

    private ObservableList<Tuteur> tuteurList = FXCollections.observableArrayList();
    private FilteredList<Tuteur> filteredTuteurs;

    private final ServiceTuteur serviceTuteur = new ServiceTuteur();

    @FXML
    public void initialize() {
        try {
            loadTuteurs();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Vérifier que searchField est bien initialisé
        if (searchTuteur != null) {
            // Création d'une liste filtrée
            filteredTuteurs = new FilteredList<>(tuteurList, tuteur -> true);

            // Ajout d'un listener pour la recherche
            searchTuteur.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredTuteurs.setPredicate(tuteur -> {
                    if (newValue == null || newValue.trim().isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();

                    return (tuteur.getCinT() != null && tuteur.getCinT().toLowerCase().contains(lowerCaseFilter)) ||
                            (tuteur.getNomT() != null && tuteur.getNomT().toLowerCase().contains(lowerCaseFilter)) ||
                            (tuteur.getPrenomT() != null && tuteur.getPrenomT().toLowerCase().contains(lowerCaseFilter)) ||
                            (tuteur.getTelephoneT() != null && tuteur.getTelephoneT().toLowerCase().contains(lowerCaseFilter));
                });
            });

            // Utilisation de SortedList pour conserver le tri des colonnes
            SortedList<Tuteur> sortedTuteurs = new SortedList<>(filteredTuteurs);
            sortedTuteurs.comparatorProperty().bind(tableTuteurs.comparatorProperty());

            // Mise à jour de la TableView avec les résultats filtrés
            tableTuteurs.setItems(sortedTuteurs);
        } else {
            System.out.println("Le champ de recherche (searchTuteur) n'est pas initialisé !");
        }
    }

    private void loadTuteurs() throws SQLException {
        List<Tuteur> tuteurs = serviceTuteur.getAllTuteurss();
        tuteurList.setAll(tuteurs);
        //ObservableList<Tuteur> observableList = FXCollections.observableArrayList(tuteurs);

        // Associer les colonnes aux attributs de l'entité Tuteur
        colId.setCellValueFactory(new PropertyValueFactory<>("idT"));
        colCin.setCellValueFactory(new PropertyValueFactory<>("cinT"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomT"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenomT"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephoneT"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresseT"));

        // Remplir la table avec les données
        tableTuteurs.setItems(tuteurList);
    }



    @FXML
    void ajouterTuteur(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterTuteur.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle et changer la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Tuteur");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void updateT(ActionEvent event) {
        Tuteur selectedTuteur = tableTuteurs.getSelectionModel().getSelectedItem();

        if (selectedTuteur != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateTuteur.fxml"));
                Parent root = loader.load();

                // Passer les données du tuteur sélectionné au contrôleur de mise à jour
                UpdateTuteurController updateController = loader.getController();
                updateController.initData(selectedTuteur);


                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Mettre à jour un Tuteur");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠️ Aucun tuteur sélectionné !");
        }
    }

    @FXML
    void deleteT(ActionEvent event) {
        // Vérifier si un tuteur est sélectionné
        Tuteur selectedTuteur = tableTuteurs.getSelectionModel().getSelectedItem();

        if (selectedTuteur == null) {
            // Afficher une alerte si aucun tuteur n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un tuteur à supprimer.");
            alert.showAndWait();
            return;
        }

        // Boîte de dialogue de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce tuteur ?");

        // Attendre la réponse de l'utilisateur
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Supprimer le tuteur de la base de données
                serviceTuteur.delete(selectedTuteur.getIdT());

                // Supprimer le tuteur de la TableView
                tableTuteurs.getItems().remove(selectedTuteur);

                // Afficher un message de succès
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Suppression réussie");
                success.setHeaderText(null);
                success.setContentText("Le tuteur a été supprimé avec succès !");
                success.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Erreur");
                error.setHeaderText(null);
                error.setContentText("Erreur lors de la suppression du tuteur.");
                error.showAndWait();
            }
        }
    }


    @FXML
    void afficherOrph(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOrphelin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Orphelins");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void exporterTuteursEnPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.setLeading(20f);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Liste des Tuteurs");
                contentStream.newLine();
                contentStream.endText();

                // Charger les tuteurs
                List<Tuteur> tuteurs = serviceTuteur.getAllTuteurss();

                // Positions pour le tableau
                float startX = 50;
                float startY = 720;
                float rowHeight = 20;
                float tableWidth = 500;
                float colWidth = tableWidth / 5; // 5 colonnes
                float marginBottom = 50; // Marge pour éviter de toucher le bas
                float textY = startY - 15;

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                float textX = startX + 5;

                // Entêtes du tableau
                String[] headers = {"CIN", "Nom", "Prénom", "Téléphone", "Adresse"};
                for (String header : headers) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(textX, textY);
                    contentStream.showText(header);
                    contentStream.endText();
                    textX += colWidth;
                }

                // Dessiner les lignes du tableau
                contentStream.setLineWidth(1f);
                float yPosition = startY;
                for (int i = 0; i <= tuteurs.size()+1; i++) {
                    contentStream.moveTo(startX, yPosition);
                    contentStream.lineTo(startX + tableWidth, yPosition);
                    contentStream.stroke();
                    yPosition -= rowHeight;
                }

                // Colonnes
                float xPosition = startX;
                for (int i = 0; i <= headers.length; i++) {
                    contentStream.moveTo(xPosition, startY);
                    contentStream.lineTo(xPosition, yPosition + rowHeight);
                    contentStream.stroke();
                    xPosition += colWidth;
                }

                // Ajouter les données des tuteurs avec gestion de la pagination
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                textY -= rowHeight;

                for (int i = 0; i < tuteurs.size(); i++) {
                    Tuteur tuteur = tuteurs.get(i);

                    // Vérifier si on doit passer à une nouvelle page
                    if (textY < marginBottom) {
                        contentStream.close();

                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);

                        textY = startY - 15;

                        // Redessiner les en-têtes
                        textX = startX + 5;
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        for (String header : headers) {
                            contentStream.beginText();
                            contentStream.newLineAtOffset(textX, textY);
                            contentStream.showText(header);
                            contentStream.endText();
                            textX += colWidth;
                        }
                        textY -= rowHeight;
                    }

                    textX = startX + 5;
                    String[] data = {
                            tuteur.getCinT(),
                            tuteur.getNomT(),
                            tuteur.getPrenomT(),
                            tuteur.getTelephoneT(),
                            tuteur.getAdresseT()
                    };

                    for (String datum : data) {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(textX, textY);
                        contentStream.showText(datum);
                        contentStream.endText();
                        textX += colWidth;
                    }
                    textY -= rowHeight;
                }

                // Assurer la fermeture du contentStream après la dernière ligne
                contentStream.close();
                document.save(file);
                System.out.println("PDF exporté avec succès !");
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }






}


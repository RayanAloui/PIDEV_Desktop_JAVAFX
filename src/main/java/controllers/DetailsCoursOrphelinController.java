package controllers;

import entities.Cours;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import services.ServiceCours;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import main.databaseconnection;

public class DetailsCoursOrphelinController {

    @FXML
    private Label lblTitre;

    @FXML
    private ImageView imgCours;

    @FXML
    private TextArea txtContenu;

    @FXML
    private TextArea txtResumeIA;

    @FXML private Label star1, star2, star3, star4, star5;
    @FXML private Label lblNoteMoyenne;
    private int currentRating = 0;
    private Cours currentCours;
    private int idOrphelin;

    @FXML
    private void initialize() {
        setupStars();
        updateNoteMoyenne();
    }

    private void setupStars() {
        Label[] stars = {star1, star2, star3, star4, star5};

        for (int i = 0; i < stars.length; i++) {
            final int rating = i + 1;
            stars[i].setOnMouseClicked(event -> setRating(rating));
        }
    }

    private void setRating(int rating) {
        currentRating = rating;
        updateStars();
    }

    private void updateStars() {
        Label[] stars = {star1, star2, star3, star4, star5};

        for (int i = 0; i < stars.length; i++) {
            if (i < currentRating) {
                stars[i].setText("★"); // Pleine étoile
            } else {
                stars[i].setText("☆"); // Étoile vide
            }
        }
    }

    @FXML
    private void enregistrerNote() {
        // Vérification si une note a été sélectionnée
        if (currentRating == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une note.");
            alert.show();
            return;
        }

        // Vérifier si currentCours et idOrphelin sont bien initialisés
        if (currentCours == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur : Aucun cours sélectionné.");
            alert.show();
            return;
        }
        if (idOrphelin == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur : Aucun orphelin sélectionné.");
            alert.show();
            return;
        }

        System.out.println("Cours sélectionné : ID = " + currentCours.getIdC());
        System.out.println("Orphelin sélectionné : ID = " + idOrphelin);

        // Requête SQL avec id_orphelin
        String query = "INSERT INTO ratings (id_orphelin, id_cours, note) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE note = ?";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idOrphelin);
            stmt.setInt(2, currentCours.getIdC());
            stmt.setInt(3, currentRating);
            stmt.setInt(4, currentRating);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Lignes affectées : " + rowsAffected);

            if (rowsAffected > 0) {
                new Alert(Alert.AlertType.INFORMATION, "✅ Note enregistrée avec succès !").show();
                updateNoteMoyenne();  // Mettre à jour la moyenne après enregistrement
                updateNoteMoyenneDansCours(currentCours.getIdC());
            } else {
                new Alert(Alert.AlertType.WARNING, "⚠️ Aucun changement effectué.").show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "❌ Erreur lors de l'enregistrement de la note.").show();
        }
    }

    public void setIdOrphelin(int idOrphelin) {
        this.idOrphelin = idOrphelin;
        System.out.println("ID Orphelin défini : " + idOrphelin); // Debug
    }

    public void setCours(Cours cours) {
        this.currentCours = cours;
        System.out.println("Cours défini dans le contrôleur : " + cours.getIdC());


        afficherDetailsCours(cours);
        updateNoteMoyenne();
    }

    private void updateNoteMoyenne() {
        if (currentCours != null) {
            ServiceCours serviceCours = new ServiceCours();
            double noteMoyenne = serviceCours.getNoteMoyenne(currentCours.getIdC());
            lblNoteMoyenne.setText("Note moyenne : " + String.format("%.1f", noteMoyenne) + " ★");
        }
    }


    public void afficherDetailsCours(Cours cours) {
        lblTitre.setText(cours.getTitre());
        txtContenu.setText(cours.getContenu());
        if (cours.getResume() != null && !cours.getResume().isEmpty()) {
            txtResumeIA.setText(cours.getResume());
        } else {
            txtResumeIA.setText("Résumé non disponible.");
        }
        // Chargement sécurisé de l'image
        String imagePath = "/" + cours.getImageC();
        InputStream imageStream = getClass().getResourceAsStream(imagePath);

        if (imageStream != null) {
            imgCours.setImage(new Image(imageStream));
        } else {
            System.out.println("⚠ Image non trouvée : " + imagePath);
        }
    }

    public void updateNoteMoyenneDansCours(int idCours) {
        String query = "UPDATE cours SET note_moyenne = (SELECT AVG(note) FROM ratings WHERE id_cours = ?) WHERE idC = ?";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idCours);
            stmt.setInt(2, idCours);

            int rowsUpdated = stmt.executeUpdate();
            System.out.println("✅ Note moyenne mise à jour pour le cours ID " + idCours + " | Lignes affectées : " + rowsUpdated);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import esprit.tn.services.ServiceOrphelin;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import esprit.tn.services.ServiceCours;
import esprit.tn.entities.Rating;

/*public class DetailsCoursTuteurController {

    @FXML
    private ImageView imgCours;
    @FXML
    private Label lblTitre;
    @FXML
    private TextArea txtContenu;
    @FXML
    private ListView<String> listOrphelins;

    @FXML private Label lblNoteMoyenne;
    @FXML private TableView<Rating> tableRatings;
    @FXML private TableColumn<Rating, String> colOrphelin;
    @FXML private TableColumn<Rating, Integer> colNote;

    private int idCours;

    private final ServiceOrphelin serviceOrphelin = new ServiceOrphelin();
    private static Cours coursSelectionne;
    private String imagePath;

    public static void setCoursSelectionne(Cours cours) {
        coursSelectionne = cours;
    }

    @FXML
    public void initialize() {
        if (coursSelectionne != null) {
            lblTitre.setText(coursSelectionne.getTitre());
            txtContenu.setText(coursSelectionne.getContenu());

            imagePath = coursSelectionne.getImageC();
            imgCours.setImage(new Image(getClass().getResourceAsStream("/" + imagePath)));

            // Charger la liste des orphelins qui ont vu ce cours
            List<String> orphelins = serviceOrphelin.getOrphelinsByCours(coursSelectionne.getIdC());
            listOrphelins.getItems().addAll(orphelins);
        }
    }

    @FXML
    private void retournerDashboard() {
        ((Stage) lblTitre.getScene().getWindow()).close();
    }

    public void afficherDetailsCours(int idCours) {
        this.idCours = idCours;

        ServiceCours serviceCours = new ServiceCours();
        lblNoteMoyenne.setText("Note moyenne : " + serviceCours.getNoteMoyenne(idCours) + " ★");

        ObservableList<Rating> ratings = serviceCours.getRatings(idCours);
        tableRatings.setItems(ratings);
    }
}*/

public class DetailsCoursTuteurController {

    @FXML private ImageView imgCours;
    @FXML private Label lblTitre;
    @FXML private TextArea txtContenu;
    @FXML private Label lblNoteMoyenne;
    @FXML private TableView<Rating> tableRatings;
    @FXML private TableColumn<Rating, String> colOrphelin;
    @FXML private TableColumn<Rating, Integer> colNote;

    private static Cours coursSelectionne;
    private final ServiceOrphelin serviceOrphelin = new ServiceOrphelin();
    private final ServiceCours serviceCours = new ServiceCours();

    public static void setCoursSelectionne(Cours cours) {
        coursSelectionne = cours;
    }

    @FXML
    public void initialize() {
        if (coursSelectionne != null) {
            lblTitre.setText(coursSelectionne.getTitre());
            txtContenu.setText(coursSelectionne.getContenu());

            // Chargement de l'image
            String imagePath = coursSelectionne.getImageC();
            imgCours.setImage(new Image(getClass().getResourceAsStream("/" + imagePath)));

            // Configurer les colonnes
            colOrphelin.setCellValueFactory(new PropertyValueFactory<>("nomOrphelin"));
            colNote.setCellValueFactory(new PropertyValueFactory<>("note"));

            // Charger les détails du cours
            afficherDetailsCours(coursSelectionne.getIdC());
        }
    }

    @FXML
    private void retournerDashboard() {
        ((Stage) lblTitre.getScene().getWindow()).close();
    }

    private void afficherDetailsCours(int idCours) {
        lblNoteMoyenne.setText("Note moyenne : " + serviceCours.getNoteMoyenne(idCours) + " ★");
        tableRatings.setItems(serviceCours.getRatings(idCours));
    }
}



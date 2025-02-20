package controllers;

import entities.Tuteur;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import entities.Orphelin;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceOrphelin;
import services.ServiceTuteur;
import javafx.scene.control.TextField;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AfficherOrphelinController {

    @FXML
    private TableView<Orphelin> tableOrphelins;
    @FXML
    private TableColumn<Orphelin, String> colNom;
    @FXML
    private TableColumn<Orphelin, String> colPrenom;
    @FXML
    private TableColumn<Orphelin, String> colDateNaissance;
    @FXML
    private TableColumn<Orphelin, String> colSexe;
    @FXML
    private TableColumn<Orphelin, String> colSituation;
    @FXML
    private TableColumn<Orphelin, String> colTuteur;
    @FXML
    private TextField searchOrphelin;
    @FXML
    private PieChart orphelinsParTuteurChart;

    private ObservableList<Orphelin> observableList = FXCollections.observableArrayList();
    private final ServiceOrphelin serviceOrphelin = new ServiceOrphelin();
    private final ServiceTuteur serviceTuteur = new ServiceTuteur();

    @FXML
    public void initialize() {
        try {
            loadOrphelins();
            setupSearchFilter();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadOrphelins() throws SQLException {
        List<Orphelin> orphelins = serviceOrphelin.getAllOrphelins();
        //ObservableList<Orphelin> observableList = FXCollections.observableArrayList(orphelins);
        observableList.setAll(orphelins);

        colNom.setCellValueFactory(new PropertyValueFactory<>("nomO"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenomO"));
        colDateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        colSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        colSituation.setCellValueFactory(new PropertyValueFactory<>("situationScolaire"));

        // Utilisation d'une CellValueFactory personnalisée pour récupérer nom/prénom du tuteur
        colTuteur.setCellValueFactory(cellData -> {
            int idTuteur = cellData.getValue().getIdTuteur();
            try {
                Tuteur tuteur = serviceTuteur.getTuteurByID(idTuteur);
                return new SimpleStringProperty(tuteur.getNomT() + " " + tuteur.getPrenomT());
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("Inconnu");
            }
        });

        tableOrphelins.setItems(observableList);
    }

    @FXML
    private void afficherStatistiques() {
        Stage stage = new Stage();
        stage.setTitle("Statistiques des Orphelins par Tuteur");

        PieChart pieChart = new PieChart();

        try {
            ServiceOrphelin serviceOrphelin = new ServiceOrphelin();
            Map<Tuteur, Integer> stats = serviceOrphelin.getOrphelinsParTuteur();

            for (Map.Entry<Tuteur, Integer> entry : stats.entrySet()) {
                String tuteurNom = entry.getKey().getNomT() + " " + entry.getKey().getPrenomT();
                int nombreOrphelins = entry.getValue();
                PieChart.Data slice = new PieChart.Data(tuteurNom + " (" + nombreOrphelins + ")", nombreOrphelins);
                pieChart.getData().add(slice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        VBox vbox = new VBox(pieChart);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void afficherBarChart() {
        Stage stage = new Stage();
        stage.setTitle("Statistiques des Orphelins par Tuteur - BarChart");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tuteur");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'Orphelins");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Orphelins par Tuteur");

        try {
            ServiceOrphelin serviceOrphelin = new ServiceOrphelin();
            Map<Tuteur, Integer> stats = serviceOrphelin.getOrphelinsParTuteur();

            int colorIndex = 0;
            String[] colors = {"#ff5733", "#33ff57", "#3357ff", "#ff33a1", "#ffff33", "#33ffff", "#ff9933"}; // Liste de couleurs

            for (Map.Entry<Tuteur, Integer> entry : stats.entrySet()) {
                String tuteurNom = entry.getKey().getNomT() + " " + entry.getKey().getPrenomT();
                int nombreOrphelins = entry.getValue();

                XYChart.Data<String, Number> data = new XYChart.Data<>(tuteurNom, nombreOrphelins);
                series.getData().add(data);

                // Appliquer une couleur unique pour chaque barre
                String color = colors[colorIndex % colors.length]; // Sélectionner une couleur cyclique
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        newNode.setStyle("-fx-bar-fill: " + color + ";");
                    }
                });

                colorIndex++; // Passer à la couleur suivante
            }
        } catch (SQLException e) {
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
    private void afficherLineChart() {
        Stage stage = new Stage();
        stage.setTitle("Statistiques des Orphelins par Tuteur - LineChart");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tuteur");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'Orphelins");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Orphelins par Tuteur");

        try {
            ServiceOrphelin serviceOrphelin = new ServiceOrphelin();
            Map<Tuteur, Integer> stats = serviceOrphelin.getOrphelinsParTuteur();

            for (Map.Entry<Tuteur, Integer> entry : stats.entrySet()) {
                String tuteurNom = entry.getKey().getNomT() + " " + entry.getKey().getPrenomT();
                int nombreOrphelins = entry.getValue();
                series.getData().add(new XYChart.Data<>(tuteurNom, nombreOrphelins));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        lineChart.getData().add(series);

        VBox vbox = new VBox(lineChart);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void setupSearchFilter() {
        FilteredList<Orphelin> filteredList = new FilteredList<>(observableList, p -> true);

        searchOrphelin.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(orphelin -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true; // Afficher tout si la recherche est vide
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Vérifier les champs correspondants
                boolean matchNom = orphelin.getNomO().toLowerCase().contains(lowerCaseFilter);
                boolean matchPrenom = orphelin.getPrenomO().toLowerCase().contains(lowerCaseFilter);
                boolean matchDateNaissance = orphelin.getDateNaissance().toLowerCase().contains(lowerCaseFilter);

                // Vérifier le tuteur
                String nomPrenomTuteur = "Inconnu";
                try {
                    Tuteur tuteur = serviceTuteur.getTuteurByID(orphelin.getIdTuteur());
                    if (tuteur != null) {
                        nomPrenomTuteur = (tuteur.getNomT() + " " + tuteur.getPrenomT()).toLowerCase();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                boolean matchTuteur = nomPrenomTuteur.contains(lowerCaseFilter);

                return matchNom || matchPrenom || matchDateNaissance || matchTuteur;
            });
        });

        SortedList<Orphelin> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableOrphelins.comparatorProperty());

        tableOrphelins.setItems(sortedList);
    }

    @FXML
    void ajouterOrphelin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterOrphelin.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle et changer la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Orphelin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void afficherTut(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherTuteur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Tuteurs");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateO(ActionEvent event) {
        Orphelin selectedOrphelin = tableOrphelins.getSelectionModel().getSelectedItem();

        if (selectedOrphelin != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateOrphelin.fxml"));
                Parent root = loader.load();

                // Passer les données du tuteur sélectionné au contrôleur de mise à jour
                UpdateOrphelinController updateController = loader.getController();
                updateController.initData(selectedOrphelin);


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

    public void deleteO(ActionEvent actionEvent) {
        // Vérifier si un orphelin est sélectionné
        Orphelin selectedOrphelin = tableOrphelins.getSelectionModel().getSelectedItem();

        if (selectedOrphelin == null) {
            // Afficher une alerte si aucun tuteur n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un orphelin à supprimer.");
            alert.showAndWait();
            return;
        }

        // Boîte de dialogue de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet orphelin ?");

        // Attendre la réponse de l'utilisateur
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Supprimer le tuteur de la base de données
                serviceOrphelin.delete(selectedOrphelin.getIdO());

                // Supprimer le tuteur de la TableView
                tableOrphelins.getItems().remove(selectedOrphelin);

                // Afficher un message de succès
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Suppression réussie");
                success.setHeaderText(null);
                success.setContentText("L'orphelin a été supprimé avec succès !");
                success.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Erreur");
                error.setHeaderText(null);
                error.setContentText("Erreur lors de la suppression d'orphelin .");
                error.showAndWait();
            }
        }
    }
}


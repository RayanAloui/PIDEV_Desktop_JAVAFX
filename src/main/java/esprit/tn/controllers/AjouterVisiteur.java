package esprit.tn.controllers;

import esprit.tn.entities.Session;
import esprit.tn.entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import esprit.tn.entities.visiteurs;
import esprit.tn.services.VisiteursService;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterVisiteur {

    @FXML
    private TextField adresse;

    @FXML
    private TextField email;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField tel;

    @FXML
    private TextField cin;



    @FXML
    public void initialize() {
        Session session = Session.getInstance();
        User currentUser= session.getCurrentUser();
        if(currentUser != null) {
            nom.setText(currentUser.getName());
            email.setText(currentUser.getEmail());
            prenom.setText(currentUser.getSurname());
            tel.setText(currentUser.getTelephone());
        }


    }
    @FXML
    void AjouterV(ActionEvent event) {
        visiteurs v = new visiteurs();
        try {
            // Récupération et nettoyage des entrées
            String adresseText = adresse.getText().trim();
            String emailText = email.getText().trim();
            String nomText = nom.getText().trim();
            String prenomText = prenom.getText().trim();
            String telText = tel.getText().trim();
            String cinText = cin.getText().trim(); // Récupération du CIN

            // Vérification que les champs ne sont pas vides
            if (nomText.isEmpty() || prenomText.isEmpty() || adresseText.isEmpty() ||
                    emailText.isEmpty() || telText.isEmpty() || cinText.isEmpty()) {
                throw new IllegalArgumentException("Tous les champs doivent être remplis !");
            }

            // Vérification du format du nom et prénom (lettres uniquement, au moins 2 caractères)
            if (!nomText.matches("[a-zA-ZÀ-ÿ\\s-]{2,}")) {
                throw new IllegalArgumentException("Le nom doit contenir uniquement des lettres et au moins 2 caractères !");
            }
            if (!prenomText.matches("[a-zA-ZÀ-ÿ\\s-]{2,}")) {
                throw new IllegalArgumentException("Le prénom doit contenir uniquement des lettres et au moins 2 caractères !");
            }

            // Vérification du format de l'email
            if (!emailText.matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$")) {
                throw new IllegalArgumentException("L'adresse email est invalide !");
            }

            // Vérification du téléphone (exactement 8 chiffres)
            if (!telText.matches("\\d{8}")) {
                throw new IllegalArgumentException("Le numéro de téléphone doit contenir exactement 8 chiffres !");
            }

            // Vérification du CIN (exactement 8 chiffres)
            if (!cinText.matches("\\d{8}")) {
                throw new IllegalArgumentException("Le CIN doit contenir exactement 8 chiffres !");
            }

            // Conversion des valeurs en entiers
            int telInt = Integer.parseInt(telText);

            // Attribution des valeurs à l'objet v
            v.setAdresse(adresseText);
            v.setEmail(emailText);
            v.setNom(nomText);
            v.setPrenom(prenomText);
            v.setTel(telInt);
            v.setCin(cinText); // Ajout du CIN à l'objet visiteurs

            // Ajout à la base de données
            VisiteursService vs = new VisiteursService();
            vs.ajouter(v);
            int visiteurId = v.getId();
            // Chargement de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visite/AjouterVisite.fxml"));
            Parent newContent = loader.load();

            // Récupération du contrôleur et passage de l'ID
            AjouterVisite controller = loader.getController();
            controller.initData(visiteurId);


            // Remplacement de la scène
            ((Node) event.getSource()).getScene().setRoot(newContent);
            // Affichage d'une alerte de succès après un ajout réussi
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Visiteur ajouté avec succès !");
                alert.showAndWait();
            });

        } catch (NumberFormatException e) {
            // Numéro trop grand ou format incorrect
            Platform.runLater(() -> {
                Alert errorAlert = new Alert(Alert.AlertType.WARNING);
                errorAlert.setTitle("Erreur de saisie");
                errorAlert.setContentText("Le numéro de téléphone et le CIN doivent être des entiers valides !");
                errorAlert.showAndWait();
            });

        } catch (IllegalArgumentException e) {
            // Autre erreur de saisie
            Platform.runLater(() -> {
                Alert errorAlert = new Alert(Alert.AlertType.WARNING);
                errorAlert.setTitle("Erreur de saisie");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            });

        } catch (SQLException e) {
            // Erreur SQL
            Platform.runLater(() -> {
                Alert sqlAlert = new Alert(Alert.AlertType.ERROR);
                sqlAlert.setTitle("Erreur Base de Données");
                sqlAlert.setContentText("Une erreur est survenue lors de l'ajout : " + e.getMessage());
                sqlAlert.showAndWait();
            });

        } catch (Exception e) {
            // Erreur inattendue
            Platform.runLater(() -> {
                Alert unknownAlert = new Alert(Alert.AlertType.ERROR);
                unknownAlert.setTitle("Erreur Inattendue");
                unknownAlert.setContentText("Une erreur s'est produite : " + e.getMessage());
                unknownAlert.showAndWait();
            });
        }
    }

    public void retourmenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visiteur/Menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nom.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

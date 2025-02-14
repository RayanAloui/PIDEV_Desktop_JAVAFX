package visites.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import visites.entities.visiteurs;
import visites.services.VisiteursService;

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
    void AjouterV(ActionEvent event){
        visiteurs v=new visiteurs();
        try {
            // Récupération et nettoyage des entrées
            String adresseText = adresse.getText().trim();
            String emailText = email.getText().trim();
            String nomText = nom.getText().trim();
            String prenomText = prenom.getText().trim();
            String telText = tel.getText().trim();

            // Vérification que les champs ne sont pas vides
            if (nomText.isEmpty() || prenomText.isEmpty() || adresseText.isEmpty() || emailText.isEmpty() || telText.isEmpty()) {
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

            // Vérification du téléphone (uniquement des chiffres)
            if (!telText.matches("\\d{8}")) {
                throw new IllegalArgumentException("Le numéro de téléphone doit contenir exactement 8 chiffres !");
            }

            // Conversion du téléphone en int
            int telInt = Integer.parseInt(telText);

            // Attribution des valeurs à l'objet v
            v.setAdresse(adresseText);
            v.setEmail(emailText);
            v.setNom(nomText);
            v.setPrenom(prenomText);
            v.setTel(telInt);

            VisiteursService vs=new VisiteursService();
            vs.ajouter(v);


        } catch (NumberFormatException e) {
            System.out.println("Erreur : Le numéro de téléphone est trop grand !");
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        catch (SQLException e) {
            Alert al=new Alert(Alert.AlertType.ERROR);
            al.setContentText(e.getMessage());
            al.showAndWait();
        }




        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Visiteur Ajouté");
        alert.setContentText("Visiteur Ajouté!");
        
        alert.show();

    }

    @FXML
    void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visiteur/AfficherVisiteur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nom.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

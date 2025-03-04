package esprit.tn.controllers;

import esprit.tn.services.UserService;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import esprit.tn.entities.User;
import esprit.tn.entities.Session;

import java.io.IOException;

public class HomeVisit {

    @FXML
    private Label email;

    @FXML
    private Label name;

    @FXML
    private ImageView profileIcon;

    @FXML
    private Button logoutButton;
    @FXML
    private AnchorPane mainPane;

    @FXML
    public void initialize() {
        Session session = Session.getInstance();

        if (session.isLoggedIn()) {
            User user = session.getCurrentUser();
            email.setText(user.getEmail());
            name.setText(user.getName());
            UserService userService = new UserService();
            String image=userService.getImage(user.getEmail());
            if(image!=null)
            {
                profileIcon.setImage(new Image(image));
            }
        } else {
            email.setText("No user logged in");
            name.setText("Guest");
        }
    }

    @FXML
    private void handleProfileIconClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editProfile.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) profileIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Could not load the profile page.");
        }
    }

    @FXML
    public void handleLogoutButtonClick(ActionEvent actionEvent) {
        Session session = Session.getInstance();
        session.clearSession();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) profileIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Could not load the profile page.");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText("An error occurred while trying to navigate.");
        alert.showAndWait();
    }


    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) name.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to load the page.");
        }
    }

    public void handleMouseEnter(MouseEvent mouseEvent) {
    }

    public void handleMouseExit(MouseEvent mouseEvent) {
    }

    public void handleActiviteClick(ActionEvent actionEvent) {
    }

    public void handleReclamationClick(ActionEvent actionEvent) {
    }

    public void handleDonClick(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
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

    public void handleVisitClick(ActionEvent actionEvent) {
    }

    public void handleOrphelinClick(ActionEvent actionEvent) {
    }


    public void manuellement(ActionEvent actionEvent) {
        try {
            // Charger la nouvelle page
            Parent root = FXMLLoader.load(getClass().getResource("/visiteur/AjouterVisiteur.fxml"));

            // Remplacer le contenu de la fenêtre principale
            mainPane.getChildren().setAll(root);

        } catch (IOException e) {
            System.out.println("erreur");;
        }

    }

    public void carte(ActionEvent actionEvent) {
        try {
            // Charger la nouvelle page
            Parent root = FXMLLoader.load(getClass().getResource("/visiteur/CarteIdentite.fxml"));

            // Remplacer le contenu de la fenêtre principale
            mainPane.getChildren().setAll(root);

        } catch (IOException e) {
            System.out.println("erreur");;
        }
    }
}

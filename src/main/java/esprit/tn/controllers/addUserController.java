package esprit.tn.controllers;

import esprit.tn.entities.User;
import esprit.tn.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class addUserController {

    @FXML
    private TextField email;

    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    @FXML
    private TextField surname;

    @FXML
    private TextField telephone;

    @FXML
    void adduser(ActionEvent event) {

        User user = new User();

        user.setName(name.getText());
        user.setSurname(surname.getText());
        user.setTelephone(telephone.getText());
        user.setEmail(email.getText());
        user.setPassword(password.getText());

        user.setBlocked(0);
        user.setConfirmed(0);
        user.setRole("visiteur");

        Random random = new Random(); int verificationNumber = 100000 + random.nextInt(900000);

        user.setNumberVerification(verificationNumber);
        user.setToken(0);

        UserService userService = new UserService();

        userService.ajouter(user);





        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("user added");
        alert.setHeaderText("User added");

        alert.showAndWait();




        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherUser.fxml")); // Replace with actual path
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to load the user list page");
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void GoToAfficherUser(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherUser.fxml"));
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

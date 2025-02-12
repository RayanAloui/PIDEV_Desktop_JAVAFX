package esprit.tn.controllers;

import esprit.tn.entities.User;
import esprit.tn.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    }

}

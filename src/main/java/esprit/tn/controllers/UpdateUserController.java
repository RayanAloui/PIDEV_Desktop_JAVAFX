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

public class UpdateUserController {

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

    private User selectedUser;


    public void setUserData(User user) {
        this.selectedUser = user;
        name.setText(user.getName());
        surname.setText(user.getSurname());
        telephone.setText(user.getTelephone());
        email.setText(user.getEmail());
        password.setText(user.getPassword());
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


    @FXML
    void Updateuser(ActionEvent event) {
        if (selectedUser != null) {

            selectedUser.setName(name.getText());
            selectedUser.setSurname(surname.getText());
            selectedUser.setTelephone(telephone.getText());
            selectedUser.setEmail(email.getText());
            selectedUser.setPassword(password.getText());


            UserService userService = new UserService();
            userService.modifier(selectedUser, selectedUser.getId());


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("User updated successfully");
            alert.showAndWait();


            GoToAfficherUser(event);
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No user selected");
            alert.setContentText("Please select a user to update.");
            alert.showAndWait();
        }
    }
}

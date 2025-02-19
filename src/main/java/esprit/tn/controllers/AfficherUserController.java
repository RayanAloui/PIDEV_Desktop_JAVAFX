package esprit.tn.controllers;

import esprit.tn.entities.User;
import esprit.tn.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.text.AbstractDocument;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class AfficherUserController {

    @FXML
    private TableView<User> tableViewUsers;

    @FXML
    private TableColumn<User, Integer> columnId;
    @FXML
    private TableColumn<User, String> columnName;
    @FXML
    private TableColumn<User, String> columnSurname;
    @FXML
    private TableColumn<User, String> columnTelephone;
    @FXML
    private TableColumn<User, String> columnEmail;
    @FXML
    private TableColumn<User, String> columnRole;
    @FXML
    private TableColumn<User, Integer> columnIsBlocked;
    @FXML
    private TableColumn<User, Integer> columnIsConfirmed;
    @FXML
    private TableColumn<User, Integer> columnNumberVerification;
    @FXML
    private TableColumn<User, String> columnToken;
    @FXML
    private ComboBox<String> comboBoxRole;







    @FXML
    public void initialize() {
         UserService userService = new UserService();


        ObservableList<User> users = FXCollections.observableArrayList(userService.getall(null));
        tableViewUsers.setItems(users);
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        columnTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        columnIsBlocked.setCellValueFactory(new PropertyValueFactory<>("isBlocked"));
        columnIsConfirmed.setCellValueFactory(new PropertyValueFactory<>("isConfirmed"));
        columnNumberVerification.setCellValueFactory(new PropertyValueFactory<>("numberVerification"));
        columnToken.setCellValueFactory(new PropertyValueFactory<>("token"));




    }
    @FXML
    void GoToAddUser(ActionEvent event) {

    }

    public void GoToAddUser(javafx.event.ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterUser.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load add user page");
            alert.showAndWait();
        }
    }

    @FXML
    void DeleteUser(ActionEvent event) {


    }


    public void DeleteUser(javafx.event.ActionEvent actionEvent) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {

            int userId = selectedUser.getId();


            UserService userService = new UserService();
            userService.supprimer(userId);


            tableViewUsers.getItems().remove(selectedUser);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("User deleted successfully");
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No user selected");
            alert.setContentText("Please select a user to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    void GoToUpdateUser(ActionEvent event) {

    }

    public void GoToUpdateUser(javafx.event.ActionEvent actionEvent) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateuser.fxml"));
                Parent root = loader.load();


                UpdateUserController controller = loader.getController();
                controller.setUserData(selectedUser);


                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();


                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to load update user page");
                alert.showAndWait();
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No User Selected");
            alert.setHeaderText("Please select a user to update");
            alert.showAndWait();
        }
    }

    @FXML
    public void filterUsersByRole() {
        String selectedRole = comboBoxRole.getValue();

        if (selectedRole == null || selectedRole.isEmpty()) {
            selectedRole = null;
        }
        UserService userService = new UserService();
        List<User> users = userService.getall(selectedRole);
        ObservableList<User> userList = FXCollections.observableArrayList(users);
        tableViewUsers.setItems(userList);
    }
}

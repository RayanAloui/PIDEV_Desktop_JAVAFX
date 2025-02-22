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
import javafx.scene.control.*;
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

        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        columnTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        columnIsBlocked.setCellValueFactory(new PropertyValueFactory<>("isBlocked"));
        columnIsBlocked.setCellFactory(column -> new TableCell<User, Integer>() {
            @Override
            protected void updateItem(Integer isBlocked, boolean empty) {
                super.updateItem(isBlocked, empty);

                if (empty || isBlocked == null) {
                    setText(null);
                } else {
                    // Convert 1 to "BLOCKED" and 0 to "ACTIVE"
                    setText(isBlocked == 1 ? "BLOCKED" : "ACTIVE");
                }
            }
        });





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

    public void GoToDashboard(javafx.event.ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
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

    public void BlockUser(javafx.event.ActionEvent actionEvent) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if (selectedUser.getRole().equals("admin") ){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Admin can t be blocked");
            alert.setHeaderText("Warning");
            alert.setContentText("You cant block administrator");
            alert.showAndWait();
        }
        if (selectedUser != null && !(selectedUser.getRole().equals("admin"))) {
            int userId = selectedUser.getId();
            UserService userService = new UserService();
            userService.BLOCK(userId);

            // Refresh the table
            refreshUserTable();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("User Blocked successfully");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No user selected");
            alert.setContentText("Please select a user to block");
            alert.showAndWait();
        }
    }

    public void ActiverUser(javafx.event.ActionEvent actionEvent) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            int userId = selectedUser.getId();
            UserService userService = new UserService();
            userService.active(userId);

            // Refresh the table
            refreshUserTable();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("User Activated successfully");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No user selected");
            alert.setContentText("Please select a user to activate");
            alert.showAndWait();
        }
    }

    private void refreshUserTable() {
        // Reload the user list after blocking or activating
        UserService userService = new UserService();
        List<User> users = userService.getall(comboBoxRole.getValue());
        ObservableList<User> userList = FXCollections.observableArrayList(users);
        tableViewUsers.setItems(userList);
    }
}

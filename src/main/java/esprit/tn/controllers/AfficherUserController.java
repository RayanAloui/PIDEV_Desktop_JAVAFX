package esprit.tn.controllers;

import esprit.tn.entities.User;
import esprit.tn.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    public void initialize() {
         UserService userService = new UserService();

        ObservableList<User> users = FXCollections.observableArrayList(userService.getall());
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
}

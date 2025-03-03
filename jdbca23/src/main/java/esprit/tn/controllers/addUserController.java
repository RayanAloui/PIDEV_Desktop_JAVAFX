package esprit.tn.controllers;

import esprit.tn.entities.User;
import esprit.tn.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private ChoiceBox<String> role;



    @FXML
    private Label nameError;
    @FXML
    private Label surnameError;
    @FXML
    private Label telephoneError;
    @FXML
    private Label emailError;
    @FXML
    private Label passwordError;
    @FXML
    private Label roleError;





    @FXML
    void adduser(ActionEvent event) {
        boolean isValid = true;

        nameError.setVisible(false);
        surnameError.setVisible(false);
        telephoneError.setVisible(false);
        emailError.setVisible(false);
        passwordError.setVisible(false);
        roleError.setVisible(false);

        if (name.getText().trim().isEmpty() || !name.getText().trim().matches("[a-zA-Z]+")) {
            nameError.setText("Name must be a string");
            nameError.setVisible(true);
            isValid = false;
        }

        if (surname.getText().trim().isEmpty() || !surname.getText().trim().matches("[a-zA-Z]+")) {
            surnameError.setText("Surname must be a string");
            surnameError.setVisible(true);
            isValid = false;
        }

        if (telephone.getText().trim().isEmpty() || !telephone.getText().trim().matches("\\d{8}")) {
            telephoneError.setText("Phone number must be 8 digits");
            telephoneError.setVisible(true);
            isValid = false;
        }

        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (email.getText().trim().isEmpty() || !email.getText().trim().matches(emailPattern)) {
            emailError.setText("Email must be in the format: exemple@exemple.com");
            emailError.setVisible(true);
            isValid = false;
        }


        if (password.getText().trim().isEmpty() || password.getText().trim().length() < 5) {
            passwordError.setText("Password must be at least 5 characters");
            passwordError.setVisible(true);
            isValid = false;
        }


        if (role.getValue() == null) {
            roleError.setText("Role is required");
            roleError.setVisible(true);
            isValid = false;
        }


        if (!isValid) {
            return;
        }


        String selectedRole = role.getValue() != null ? role.getValue() : "client";

        User user = new User();
        user.setName(name.getText());
        user.setSurname(surname.getText());
        user.setTelephone(telephone.getText());
        user.setEmail(email.getText());
        UserService X=new UserService();
        String cryptedPass= X.CRYPTE(password.getText());
        user.setPassword(cryptedPass);
        user.setRole(selectedRole);

        user.setBlocked(0);
        user.setConfirmed(0);

        Random random = new Random();
        int verificationNumber = 100000 + random.nextInt(900000);
        user.setNumberVerification(verificationNumber);
        user.setToken(0);

        UserService userService = new UserService();
        userService.ajouter(user);


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Added");
        alert.setHeaderText("User added successfully");
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

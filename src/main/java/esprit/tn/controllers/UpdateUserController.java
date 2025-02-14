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

    @FXML
    private ChoiceBox<String> role;

    @FXML
    private TextField isBlocked;

    @FXML
    private TextField isConfirmed;

    @FXML
    private TextField numberVerification;

    @FXML
    private TextField token;
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
    private Label isBlockedError;

    @FXML
    private Label isConfirmedError;

    @FXML
    private Label numberVerificationError;

    @FXML
    private Label tokenError;

    private User selectedUser;



    public void setUserData(User user) {
        this.selectedUser = user;
        name.setText(user.getName());
        surname.setText(user.getSurname());
        telephone.setText(user.getTelephone());
        email.setText(user.getEmail());
        password.setText(user.getPassword());
        role.setValue(user.getRole());
        isBlocked.setText(String.valueOf(user.getIsBlocked()));
        isConfirmed.setText(String.valueOf(user.getIsConfirmed()));
        numberVerification.setText(String.valueOf(user.getNumberVerification()));
        token.setText(String.valueOf(user.getToken()));
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
        // Reset all error messages
        nameError.setVisible(false);
        surnameError.setVisible(false);
        telephoneError.setVisible(false);
        emailError.setVisible(false);
        passwordError.setVisible(false);
        roleError.setVisible(false);
        isBlockedError.setVisible(false);
        isConfirmedError.setVisible(false);
        numberVerificationError.setVisible(false);
        tokenError.setVisible(false);

        boolean isValid = true;

        // Validate Name
        if (name.getText().trim().isEmpty() || !name.getText().trim().matches("[a-zA-Z]+")) {
            nameError.setText("Name must be a string");
            nameError.setVisible(true);
            isValid = false;
        }

        // Validate Surname
        if (surname.getText().trim().isEmpty() || !surname.getText().trim().matches("[a-zA-Z]+")) {
            surnameError.setText("Surname must be a string");
            surnameError.setVisible(true);
            isValid = false;
        }

        // Validate Phone Number
        if (telephone.getText().trim().isEmpty() || !telephone.getText().trim().matches("\\d{8}")) {
            telephoneError.setText("Phone number must be 8 digits");
            telephoneError.setVisible(true);
            isValid = false;
        }

        // Validate Email
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (email.getText().trim().isEmpty() || !email.getText().trim().matches(emailPattern)) {
            emailError.setText("Email must be in the format: exemple@exemple.com");
            emailError.setVisible(true);
            isValid = false;
        }

        // Validate Password
        if (password.getText().trim().isEmpty() || password.getText().trim().length() < 5) {
            passwordError.setText("Password must be at least 5 characters");
            passwordError.setVisible(true);
            isValid = false;
        }

        // Validate Role
        if (role.getValue() == null) {
            roleError.setText("Role is required");
            roleError.setVisible(true);
            isValid = false;
        }

        // Validate Is Blocked
        if (isBlocked.getText().trim().isEmpty() || !isBlocked.getText().trim().matches("[01]")) {
            isBlockedError.setText("Is Blocked must be 0 or 1");
            isBlockedError.setVisible(true);
            isValid = false;
        }

        // Validate Is Confirmed
        if (isConfirmed.getText().trim().isEmpty() || !isConfirmed.getText().trim().matches("[01]")) {
            isConfirmedError.setText("Is Confirmed must be 0 or 1");
            isConfirmedError.setVisible(true);
            isValid = false;
        }

        // Validate Number Verification
        if (numberVerification.getText().trim().isEmpty() || !numberVerification.getText().trim().matches("\\d+")) {
            numberVerificationError.setText("Number Verification must be a number");
            numberVerificationError.setVisible(true);
            isValid = false;
        }

        // Validate Token
        if (token.getText().trim().isEmpty() || !token.getText().trim().matches("\\d+")) {
            tokenError.setText("Token must be a number");
            tokenError.setVisible(true);
            isValid = false;
        }

        // If any validation fails, stop the update process
        if (!isValid) {
            return;
        }

        // Update the user if all fields are valid
        if (selectedUser != null) {
            selectedUser.setName(name.getText());
            selectedUser.setSurname(surname.getText());
            selectedUser.setTelephone(telephone.getText());
            selectedUser.setEmail(email.getText());
            selectedUser.setPassword(password.getText());
            selectedUser.setRole(role.getValue());
            selectedUser.setIsBlocked(Integer.parseInt(isBlocked.getText()));
            selectedUser.setIsConfirmed(Integer.parseInt(isConfirmed.getText()));
            selectedUser.setNumberVerification(Integer.parseInt(numberVerification.getText()));
            selectedUser.setToken(Integer.parseInt(token.getText()));

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

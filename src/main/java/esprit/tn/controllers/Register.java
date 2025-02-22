package esprit.tn.controllers;

import esprit.tn.entities.Notification;
import esprit.tn.entities.User;
import esprit.tn.services.NotificationService;
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
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Register {



    @FXML
    private TextField email;

    @FXML
    private Label emailError;

    @FXML
    private TextField name;

    @FXML
    private Label nameError;

    @FXML
    private PasswordField password;

    @FXML
    private Label passwordError;

    @FXML
    private PasswordField passwordconfirmation;

    @FXML
    private Button registerButton;

    @FXML
    private TextField surname;

    @FXML
    private Label surnameError;

    @FXML
    private TextField telephone;

    @FXML
    private Label telephoneError;

    @FXML
    void GoToLogin(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


            stage.setScene(new Scene(root));


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load to login page");
            alert.setContentText("An error occurred while trying to navigate back to the user list.");
            alert.showAndWait();
        }

    }

    @FXML
    void adduser(ActionEvent event) {
        boolean isValid = true;

        nameError.setVisible(false);
        surnameError.setVisible(false);
        telephoneError.setVisible(false);
        emailError.setVisible(false);
        passwordError.setVisible(false);


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

        if ( ! password.getText().trim().equals(passwordconfirmation.getText())) {
            passwordError.setText("Password not confirmed");
            passwordError.setVisible(true);
            isValid = false;
        }


        // Check if email already exists in the database
        UserService userService1 = new UserService();
        if (userService1.emailExists(email.getText().trim())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Email already exists");
            alert.setContentText("The email you entered is already in use. Please use a different email.");
            alert.showAndWait();
            return;
        }




        if (!isValid) {
            return;
        }




        User user = new User();
        user.setName(name.getText());
        user.setSurname(surname.getText());
        user.setTelephone(telephone.getText());
        user.setEmail(email.getText());
        user.setPassword(password.getText());
        user.setRole("client");

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

        Notification notification = new Notification();

        notification.setUsername(user.getName());
        notification.setActivite("signed in ");
        String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        notification.setHeure(formattedTime);
        notification.setDate(Date.valueOf(LocalDate.now()));


        NotificationService notificationService = new NotificationService();
        notificationService.ajouter(notification);


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml")); // Replace with actual path
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

}

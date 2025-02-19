package esprit.tn.controllers;

import esprit.tn.entities.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.prefs.Preferences;
import  esprit.tn.entities.User;

import esprit.tn.main.DatabaseConnection;

public class Login {

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField password;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Hyperlink signUpLink;

    @FXML
    private TextField email;
    @FXML
    public void initialize() {
        Preferences prefs = Preferences.userNodeForPackage(Login.class);
        String savedEmail = prefs.get("email", "");
        String savedPassword = prefs.get("password", "");

        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            email.setText(savedEmail);
            password.setText(savedPassword);
            rememberMeCheckBox.setSelected(true);
        }
    }

    @FXML
    void GoToForgetPWD(ActionEvent event) {

    }

    @FXML
    void GoToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    void login(ActionEvent event) {
        Connection cnx = DatabaseConnection.getInstance().getCnx();
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(query);
            preparedStatement.setString(1, email.getText());
            preparedStatement.setString(2, password.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int isBlocked = resultSet.getInt("isBlocked");

                if (isBlocked == 1) {
                    showAlert(Alert.AlertType.WARNING, "Account Blocked", "Your account has been blocked. Please contact support.");
                } else {
                    User user = new User();

                    // Set all fields using setters
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setTelephone(resultSet.getString("telephone"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getString("role"));
                    user.setIsBlocked(resultSet.getInt("isBlocked"));
                    user.setIsConfirmed(resultSet.getInt("isConfirmed"));
                    user.setNumberVerification(resultSet.getInt("numberVerification"));
                    user.setToken(resultSet.getInt("token"));

                    // Set the user in the session
                    Session session = Session.getInstance();
                    session.setCurrentUser(user);
                    // Check the user's role and navigate accordingly
                    if ("admin".equals(user.getRole())) {
                        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, Admin " + user.getName() + "!");
                        navigateToPage(event, "/dashboard.fxml");
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + user.getName() + "!");
                        navigateToPage(event, "/home.fxml");
                    }

                    // Save credentials if "Remember Me" is checked
                    if (rememberMeCheckBox.isSelected()) {
                        Preferences prefs = Preferences.userNodeForPackage(Login.class);
                        prefs.put("email", email.getText());
                        prefs.put("password", password.getText());
                    } else {
                        // Clear saved credentials if "Remember Me" is not checked
                        Preferences prefs = Preferences.userNodeForPackage(Login.class);
                        prefs.remove("email");
                        prefs.remove("password");
                    }
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while trying to log in.");
        }
    }

    private void navigateToPage(ActionEvent event, String fxmlPath) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
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

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

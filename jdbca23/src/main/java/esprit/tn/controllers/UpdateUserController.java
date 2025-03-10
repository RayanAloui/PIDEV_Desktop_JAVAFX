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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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



    private User selectedUser;
    @FXML
    private ImageView picture;



    public void setUserData(User user) {
        this.selectedUser = user;
        name.setText(user.getName());
        surname.setText(user.getSurname());
        telephone.setText(user.getTelephone());
        email.setText(user.getEmail());
        UserService X=new UserService();
        String realPass=X.DECRYPTE(user.getPassword());
        password.setText(realPass);
        role.setValue(user.getRole());
        UserService userService = new UserService();
        String image=userService.getImage(user.getEmail());
        if(image!=null)
        {
            picture.setImage(new Image(image));
        }else{picture.setImage(new Image("profile.png"));}

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
            //
            UserService X=new UserService();
            String cryptedPass = X.CRYPTE( password.getText());
            selectedUser.setPassword(cryptedPass);
            selectedUser.setRole(role.getValue());


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

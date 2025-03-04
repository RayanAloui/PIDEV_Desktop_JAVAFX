package esprit.tn.controllers;

import esprit.tn.services.ITuteurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Date;  // Import java.sql.Date
import java.util.Optional;
import java.util.Random;

import esprit.tn.entities.Reclamation;
import esprit.tn.services.SmsService1;

public class Userreclamtionpage {

    @FXML
    private TextArea description;

    @FXML
    private Label descriptionError;

    @FXML
    private TextField mail;

    @FXML
    private Label mailError;

    @FXML
    private ChoiceBox<String> typereclamation;

    @FXML
    private Label typereclamationError;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label dateError;

    @FXML
    public void initialize() {
        // Initialize error labels to be invisible by default
        descriptionError.setVisible(false);
        mailError.setVisible(false);
        typereclamationError.setVisible(false);
        dateError.setVisible(false);

        // Set default text for error labels (optional)
        descriptionError.setText("Description is required");
        mailError.setText("Email is required");
        typereclamationError.setText("Typereclamation is required");
        dateError.setText("Date is required");

        // Clear any existing text in input fields (optional)
        description.clear();
        mail.clear();
        typereclamation.getSelectionModel().clearSelection();
        datePicker.setValue(null);
    }

    @FXML
    void GoToAfficherReclamations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherreclamtions.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the reclamation page");
            alert.showAndWait();
        }
    }

    @FXML
    void addReclamation(ActionEvent event) {
        // First, check CAPTCHA
        if (!showCaptchaDialog()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("CAPTCHA Verification Failed");
            alert.setHeaderText("The CAPTCHA verification failed");
            alert.setContentText("Please enter the correct CAPTCHA to proceed.");
            alert.showAndWait();
            return; // Exit if CAPTCHA is incorrect
        }

        boolean isValid = true;

        // Reset error messages
        mailError.setVisible(false);
        descriptionError.setVisible(false);
        typereclamationError.setVisible(false);
        dateError.setVisible(false);

        // Validate email
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (mail.getText().trim().isEmpty() || !mail.getText().trim().matches(emailPattern)) {
            mailError.setText("Email must be in the format: example@example.com");
            mailError.setVisible(true);
            isValid = false;
        }

        // Validate description
        if (description.getText().trim().isEmpty()) {
            descriptionError.setText("Description is required");
            descriptionError.setVisible(true);
            isValid = false;
        }

        // Validate typereclamation
        if (typereclamation.getValue() == null) {
            typereclamationError.setText("Typereclamation is required");
            typereclamationError.setVisible(true);
            isValid = false;
        }

        // Validate date
        if (datePicker.getValue() == null) {
            dateError.setText("Date is required");
            dateError.setVisible(true);
            isValid = false;
        }

        // If validation fails, stop further execution
        if (!isValid) {
            return;
        }

        // Create a new Reclamation object
        Reclamation reclamation = new Reclamation();
        reclamation.setMail(mail.getText());
        reclamation.setDescription(description.getText());
        reclamation.setTypereclamation(typereclamation.getValue());
        reclamation.setDate(Date.valueOf(datePicker.getValue())); // Set the selected date

        // Add reclamation to the database
        ITuteurService.ReclamationService reclamationService = new ITuteurService.ReclamationService();
        try {
            reclamationService.ajouter(reclamation);

            // Send SMS after adding reclamation (with the reclamation ID)
            SmsService1 envoyerSmsService = new SmsService1();
            String message = "Merci pour votre réclamation. L'ID de votre réclamation est: " + reclamation.getId();
            String phoneNumber = "+21655732015"; // Replace with the user's phone number
            envoyerSmsService.envoyerSMS(phoneNumber, message);

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reclamation Added");
            alert.setHeaderText("Reclamation added successfully");
            alert.setContentText("ID de votre réclamation: " + reclamation.getId());
            alert.showAndWait();

            // Redirect to the reclamation list page
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherReclamations.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Failed to load the reclamation list page");
                errorAlert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to add reclamation");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    // CAPTCHA methods
    private String generateCaptcha() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 6; i++) { // Length of CAPTCHA: 6 characters
            int index = random.nextInt(characters.length());
            captcha.append(characters.charAt(index));
        }
        return captcha.toString();
    }

    private boolean showCaptchaDialog() {
        String captcha = generateCaptcha(); // Generate a new CAPTCHA

        // Create a dialog box
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("CAPTCHA Verification");
        dialog.setHeaderText("Please enter the following code:");

        // Add buttons
        ButtonType validateButtonType = new ButtonType("Validate", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(validateButtonType, ButtonType.CANCEL);

        // Create a Pane to display the CAPTCHA
        Pane captchaPane = new Pane();
        captchaPane.setPrefSize(300, 100);

        // Add lines and points as background
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Line line = new Line(
                    random.nextInt(300), random.nextInt(100),
                    random.nextInt(300), random.nextInt(100)
            );
            line.setStroke(Color.LIGHTGRAY);
            captchaPane.getChildren().add(line);
        }

        // Add text for the CAPTCHA with effects
        double x = 20;
        for (char c : captcha.toCharArray()) {
            Text text = new Text(String.valueOf(c));
            text.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            text.setFill(Color.BLACK);
            text.setEffect(new DropShadow(5, Color.GRAY));

            // Random rotation
            text.setRotate(random.nextInt(30) - 15);

            // Random vertical positioning
            text.setY(50 + random.nextInt(20) - 10);

            text.setX(x);
            x += 30; // Spacing between characters

            captchaPane.getChildren().add(text);
        }

        // Add an input field for the CAPTCHA
        TextField captchaField = new TextField();
        captchaField.setPromptText("Enter CAPTCHA code");

        // Add CAPTCHA and the input field in the dialog box
        dialog.getDialogPane().setContent(new VBox(10, captchaPane, captchaField));

        // Handle the result
        dialog.setResultConverter(buttonType -> {
            if (buttonType == validateButtonType) {
                return captchaField.getText();
            }
            return null;
        });

        // Display the dialog and verify the input
        Optional<String> result = dialog.showAndWait();
        return result.isPresent() && result.get().equals(captcha);
    }}

package esprit.tn.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import esprit.tn.entities.Reponse;
import esprit.tn.services.ReponseService;

public class updatereponseController {




        @FXML
        private TextArea description;

        @FXML
        private DatePicker date;

        @FXML
        private TextField mail;

        @FXML
        private ChoiceBox<String> statut;

        @FXML
        private Label descriptionError;

        @FXML
        private Label dateError;

        @FXML
        private Label mailError;

        @FXML
        private Label statutError;

        private Reponse selectedReponse;

        public void setReponseData(Reponse reponse) {
            this.selectedReponse = reponse;
            description.setText(reponse.getDescription());

            date.setValue(LocalDate.parse(reponse.getDate().toString()));  // Correctly set LocalDate
            statut.setValue(reponse.getStatut());
        }

        @FXML
        public void GoToAfficherreponse(ActionEvent actionEvent) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherReponse.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not load the reponse page");
                alert.showAndWait();
            }
        }

        @FXML
        public void Updatereponse(ActionEvent event) {
            descriptionError.setVisible(false);
            dateError.setVisible(false);
            mailError.setVisible(false);
            statutError.setVisible(false);

            boolean isValid = true;

            // Validating inputs
            if (description.getText().trim().isEmpty()) {
                descriptionError.setText("Description is required");
                descriptionError.setVisible(true);
                isValid = false;
            }
            if (date.getValue() == null) {
                dateError.setText("Date is required");
                dateError.setVisible(true);
                isValid = false;
            }
            String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (mail.getText().trim().isEmpty() || !mail.getText().trim().matches(emailPattern)) {
                mailError.setText("Invalid email format");
                mailError.setVisible(true);
                isValid = false;
            }
            if (statut.getValue() == null) {
                statutError.setText("Status is required");
                statutError.setVisible(true);
                isValid = false;
            }

            if (!isValid) {
                return;
            }

            if (selectedReponse != null) {
                // Convert LocalDate to java.sql.Date
                Date sqlDate = Date.valueOf(date.getValue());  // Converting LocalDate to java.sql.Date

                // Update the Reponse object with new data
                selectedReponse.setDescription(description.getText());
                selectedReponse.setDate(sqlDate);  // Store date as java.sql.Date in the model
                selectedReponse.setStatut(statut.getValue());

                ReponseService reponseService = new ReponseService();
                reponseService.modifier(selectedReponse);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Reponse updated successfully");
                alert.showAndWait();

                GoToAfficherreponse(event);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("No reponse selected");
                alert.showAndWait();
            }
        }
    }


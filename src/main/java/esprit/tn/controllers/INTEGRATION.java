package esprit.tn.controllers;import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class INTEGRATION {

    private void loadPage(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void GotoUserPage() {
        loadPage("/AfficherUser.fxml");
    }

    @FXML
    private void GotoOrphelinPage() {
        loadPage("/AfficherOrphelin.fxml");
    }

    @FXML
    private void GotoDonPage() {
        loadPage("/AfficherDonateur.fxml");
    }

    @FXML
    private void GotoVisitPage() {
        loadPage("/visiteur/AfficherVisiteur.fxml");
    }

    @FXML
    private void GotoReclamationPage() {
        loadPage("/AfficherReclamation.fxml");
    }

    @FXML
    private void GotoActivitePage() {
        loadPage("/AfficherActivite.fxml");
    }
}

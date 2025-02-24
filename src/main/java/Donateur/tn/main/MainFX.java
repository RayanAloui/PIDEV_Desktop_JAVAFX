package Donateur.tn.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.util.Objects;

public class MainFX extends Application {
    @Override
    public void start(Stage PrimaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AfficherDonateur.fxml")));
        Scene scene = new Scene(root);
        PrimaryStage.setScene(scene);
        PrimaryStage.setTitle("Ajouter donateur");
        PrimaryStage.show();

    }
    public static void main(String[] args) {
        //Cette méthode appelle automatiquement start(Stage PrimaryStage)
        //et lance l'application JavaFX

        launch(args);
    }

}

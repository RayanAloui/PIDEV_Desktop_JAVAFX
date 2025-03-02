package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load your FXML file here
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherreponse.fxml"));
        AnchorPane root = loader.load();

        // Set the scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion Reclamations");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
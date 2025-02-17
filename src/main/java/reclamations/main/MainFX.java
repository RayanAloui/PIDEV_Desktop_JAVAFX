package reclamations.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file that corresponds to your GUI layout
        Parent root = FXMLLoader.load(getClass().getResource("/afficherreclamations.fxml")); // Fixed missing quote here

        Scene scene = new Scene(root);

        // Set the window title
        primaryStage.setTitle("Gestion des Réclamations");

        // Optional: Set the window size
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        // Set the scene and show the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}

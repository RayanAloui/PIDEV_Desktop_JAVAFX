package visites.tn.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root= FXMLLoader.load(getClass().getResource("/visiteur/Menu.fxml"));
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ajouter Visiteur");
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}

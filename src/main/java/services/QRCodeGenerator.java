package services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import entities.Tuteur;

public class QRCodeGenerator {

    public static String generateQRCode(Tuteur tuteur, String filename) {
        int width = 150;
        int height = 150;
        String directory = "src/main/resources";
        String filePath = directory + filename + ".png";

        try {
            // Vérifier et créer le dossier s'il n'existe pas
            Path dirPath = Paths.get(directory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // Construire les informations du tuteur sous forme de texte clair
            String data = "Tuteur Informations :\n"
                    + "CIN : " + tuteur.getCinT() + "\n"
                    + "Nom : " + tuteur.getNomT() + "\n"
                    + "Prénom : " + tuteur.getPrenomT() + "\n"
                    + "Téléphone : " + tuteur.getTelephoneT() + "\n"
                    + "Adresse : " + tuteur.getAdresseT() + "\n"
                    + "Email : " + tuteur.getEmail() + "\n"
                    + "Disponibilité : " + tuteur.getDisponibilite();

            // Générer le QR code
            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height);
            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(matrix, "PNG", path);
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


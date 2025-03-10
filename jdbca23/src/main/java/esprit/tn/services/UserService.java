package esprit.tn.services;

import esprit.tn.entities.User;
import esprit.tn.main.DatabaseConnection;

import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import javafx.scene.image.Image;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class UserService implements Iservice<User> {
    Connection cnx;

    public UserService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(User user) {
        String req = "INSERT INTO user (name, surname, telephone, email, password, role, isBlocked, isConfirmed, numberVerification , token) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? , ?)";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, user.getName());
            stm.setString(2, user.getSurname());
            stm.setString(3, user.getTelephone());
            stm.setString(4, user.getEmail());
            stm.setString(5, user.getPassword());
            stm.setString(6, user.getRole());
            stm.setInt(7, user.isBlocked());
            stm.setInt(8, user.isConfirmed());
            stm.setInt(9, user.getNumberVerification());
            stm.setInt(10, user.getToken());

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(User user, int id) {

        String req = "UPDATE user SET name=?, surname=?, telephone=?, email=?, password=?, role=?, isBlocked=?, isConfirmed=?, numberVerification=? , token=? WHERE id=?";

        try {

            PreparedStatement stm = cnx.prepareStatement(req);


            stm.setString(1, user.getName());
            stm.setString(2, user.getSurname());
            stm.setString(3, user.getTelephone());
            stm.setString(4, user.getEmail());
            stm.setString(5, user.getPassword());
            stm.setString(6, user.getRole());
            stm.setInt(7, user.isBlocked());
            stm.setInt(8, user.isConfirmed());
            stm.setInt(9, user.getNumberVerification());


            stm.setInt(10, user.getToken());
            stm.setInt(11, id);

          
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM user WHERE id=?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<User> getall(String filter) {
        List<User> users = new ArrayList<>();
        String req;

        // List of valid roles
        List<String> roles = Arrays.asList("admin", "client", "visiteur", "tuteur", "donateur");

        if (filter == null) {
            req = "SELECT * FROM user";
        } else if (roles.contains(filter)) {
            req = "SELECT * FROM user WHERE role = '" + filter + "'";
        } else {
            req = "SELECT * FROM user WHERE name = '" + filter + "' OR email = '" + filter + "' OR surname = '" + filter + "'";
        }

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("isBlocked"),
                        rs.getInt("isConfirmed"),
                        rs.getInt("numberVerification"),
                        rs.getInt("token")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }


    @Override
    public User getone(int id) {
        String req = "SELECT * FROM user WHERE id=?";
        User user = null;

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("isBlocked"),
                        rs.getInt("isConfirmed"),
                        rs.getInt("numberVerification"),
                        rs.getInt("token")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }


    @Override
    public User getoneByEmail(String email) {
        String req = "SELECT * FROM user WHERE email=?";
        User user = null;

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("isBlocked"),
                        rs.getInt("isConfirmed"),
                        rs.getInt("numberVerification"),
                        rs.getInt("token")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public boolean updatePassword(int userId, String newPassword) {
        // Assuming you have a DatabaseConnection class to interact with the database
        String query = "UPDATE user SET password = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getCnx();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            // Set the new password and user ID
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userId);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            // If rows were affected, the update was successful
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // If an error occurs, return false
        }
    }


    public boolean emailExists(String email) {
        String query = "SELECT id FROM user WHERE email = ?"; // Fetching the ID instead of COUNT(*)

        try (PreparedStatement pstmt = cnx.prepareStatement(query)) { // Use cnx instead of creating a new connection
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id"); // Retrieve the ID of the found user
                return getone(userId) != null; // Check if the user exists using getone method
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log error properly in a real application
        }
        return false; // Return false if no user is found or an error occurs
    }

    public boolean telephoneExists(String phoneNumber) {
        String query = "SELECT id FROM user WHERE telephone = ?"; // Fetching the ID based on the phone number

        try (PreparedStatement pstmt = cnx.prepareStatement(query)) { // Use existing connection (cnx)
            pstmt.setString(1, phoneNumber); // Set the phone number to the query
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id"); // Retrieve the ID of the found user
                return getone(userId) != null; // Check if the user exists using the getone method
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log error properly in a real application
        }
        return false; // Return false if no user is found or an error occurs
    }
    public User telephoneExists1(String phoneNumber) {
        String query = "SELECT id FROM user WHERE telephone = ?"; // Fetching the ID based on the phone number

        try (PreparedStatement pstmt = cnx.prepareStatement(query)) { // Use existing connection (cnx)
            pstmt.setString(1, phoneNumber); // Set the phone number to the query
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id"); // Retrieve the ID of the found user

                // Now fetch the full user details based on the user ID
                String req = "SELECT * FROM user WHERE id=?";
                try (PreparedStatement stm = cnx.prepareStatement(req)) {
                    stm.setInt(1, userId);
                    ResultSet resultSet = stm.executeQuery();

                    if (resultSet.next()) {
                        return new User(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("surname"),
                                resultSet.getString("telephone"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                resultSet.getString("role"),
                                resultSet.getInt("isBlocked"),
                                resultSet.getInt("isConfirmed"),
                                resultSet.getInt("numberVerification"),
                                resultSet.getInt("token")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log error properly in a real application
        }
        return null; // Return null if no user is found or an error occurs
    }

    public void BLOCK(int userId) {
        String query = "UPDATE user SET isBlocked = 1 WHERE id = ?"; // Set isBlocked to 1 for blocking the user

        try {
            PreparedStatement stm = cnx.prepareStatement(query);
            stm.setInt(1, userId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error blocking user", e);
        }
    }

    public void active(int userId) {
        String query = "UPDATE user SET isBlocked = 0 WHERE id = ?"; // Set isBlocked to 0 for activating the user

        try {
            PreparedStatement stm = cnx.prepareStatement(query);
            stm.setInt(1, userId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error activating user", e);
        }
    }
    public void confirm(int userId) {
        String query = "UPDATE user SET isConfirmed= 1 WHERE id = ?"; // Set isBlocked to 1 for blocking the user

        try {
            PreparedStatement stm = cnx.prepareStatement(query);
            stm.setInt(1, userId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error blocking user", e);
        }
    }

























    public void generatePDF(List<User> users) {
        String filePath = "users.pdf";

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Ajouter un titre
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Liste des Utilisateurs", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Ajouter la date et l'heure de génération
            String dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            Font dateFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            Paragraph dateParagraph = new Paragraph("Généré le : " + dateTime, dateFont);
            dateParagraph.setAlignment(Element.ALIGN_RIGHT);
            dateParagraph.setSpacingAfter(10);
            document.add(dateParagraph);

            // Création du tableau
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            // Ajout des en-têtes de colonnes
            addTableHeader(table);

            // Ajout des lignes du tableau
            for (User user : users) {
                table.addCell(String.valueOf(user.getId()));
                table.addCell(user.getName());
                table.addCell(user.getSurname());
                table.addCell(user.getTelephone());
                table.addCell(user.getEmail());
                table.addCell(user.getRole());
            }

            document.add(table);

            // Ajouter une image en bas (signature)
            com.itextpdf.text.Image signature = com.itextpdf.text.Image.getInstance("src/main/resources/signature.png");
            signature.scaleAbsolute(150, 50);  // Utiliser scaleAbsolute au lieu de scaleToFit
            signature.setAlignment(Element.ALIGN_CENTER);
            signature.setSpacingBefore(20);

            signature.scaleToFit(150, 50);
            signature.setAlignment(Element.ALIGN_CENTER);
            signature.setSpacingBefore(20);
            document.add(signature);

            document.close();
            System.out.println("PDF généré avec succès : " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTableHeader(PdfPTable table) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        String[] headers = {"ID", "Nom", "Prénom", "Téléphone", "Email", "Rôle"};

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
    }




    public String CRYPTE(String password) {
        // Set the key to 10
        int key = 10;

        StringBuilder encryptedPassword = new StringBuilder();

        // Loop through each character and apply the ASCII shift by the key
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            // Shift the ASCII value of the character by the key
            encryptedPassword.append((char) (ch + key));
        }

        // Append the key at the end in the format key and use '\u001F' as separator
        encryptedPassword.append((char) 0x1F); // Using the Unit Separator ASCII character
        encryptedPassword.append(key);

        System.out.println("Encrypted Password: " + encryptedPassword.toString());
        return encryptedPassword.toString();
    }

    // Method to decrypt the password by reversing the shift using the key
    public String DECRYPTE(String encryptedPassword) {
        // Find the separator position (character \u001F)
        int separatorIndex = encryptedPassword.indexOf((char) 0x1F);

        // If separator is not found, return null
        if (separatorIndex == -1) {
            System.out.println("Separator not found.");
            return null;
        }

        // Extract the key from the part after the separator
        int key = Integer.parseInt(encryptedPassword.substring(separatorIndex + 1));

        // Decrypt the password by reversing the ASCII shift by the key
        StringBuilder decryptedPassword = new StringBuilder();
        for (int i = 0; i < separatorIndex; i++) {
            char ch = encryptedPassword.charAt(i);
            // Reverse the shift by subtracting the key from the character's ASCII value
            decryptedPassword.append((char) (ch - key));
        }

        System.out.println("Decrypted Password: " + decryptedPassword.toString());
        return decryptedPassword.toString();
    }
    // Check if the user exists
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(

                        resultSet.getString("email")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User addGoogleUser(String nameGU, String emailGU) {
        if (emailGU == null || emailGU.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        // Check if the user already exists in the database
        User existingUser = getUserByEmail(emailGU);
        if (existingUser != null) {
            return existingUser; // User already exists, return it
        }

        // Prepare the SQL query to insert the new user with default values
        String insertQuery = "INSERT INTO user (name, surname, telephone, email, password, role, isBlocked, isConfirmed, numberVerification, token) "
                + "VALUES (?, ?, ?, ?, 0, ?, 0, 0, 0, 0)";

        try (PreparedStatement statement = cnx.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Set the parameters for the SQL query
            statement.setString(1, nameGU); // Google user's name
            statement.setString(2, "GOOGLE"); // Default empty surname
            statement.setString(3, ""); // Default empty telephone
            statement.setString(4, emailGU); // Google user's email
            statement.setString(5, "client"); // Default role

            // Execute the update and get the generated keys
            statement.executeUpdate();

            // Get the generated user ID (auto-increment value)
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                // Return a new User object with the provided email and default values
                return new User(
                        generatedKeys.getInt(1), // Set the generated ID
                        nameGU, // Google user's name
                        "", // Default surname
                        "", // Default telephone
                        emailGU, // Google user's email
                        "", // Default password
                        "user", // Default role
                        0, // Default isBlocked
                        0, // Default isConfirmed
                        0, // Default numberVerification
                        0  // Default token
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }










    public String getImage(String email) {
        String query = "SELECT image FROM user WHERE email = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("image");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertImage(String email, String imagePath) {
        String query = "UPDATE user SET image = ? WHERE email = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, imagePath);
            statement.setString(2, email);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }










}

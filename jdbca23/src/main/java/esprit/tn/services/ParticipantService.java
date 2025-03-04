package esprit.tn.services;

import esprit.tn.entities.Participant;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantService implements Iservice0<Participant> {
    Connection cnx;

    public ParticipantService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Participant participant) {
        String req = "INSERT INTO participant (nom, prenom, age) VALUES (?, ?, ?)";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, participant.getNom());
            stm.setString(2, participant.getPrenom());
            stm.setInt(3, participant.getAge());

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(Participant participant, int id) {
        String req = "UPDATE participant SET nom=?, prenom=?, age=? WHERE id=?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, participant.getNom());
            stm.setString(2, participant.getPrenom());
            stm.setInt(3, participant.getAge());
            stm.setInt(4, id);

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM participant WHERE id=?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Participant> getall() {
        List<Participant> participants = new ArrayList<>();
        String req = "SELECT * FROM participant";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()) {
                Participant participant = new Participant(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("age")
                );
                participants.add(participant);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return participants;
    }

    @Override
    public Participant getone(int id) {
        String req = "SELECT * FROM participant WHERE id=?";
        Participant participant = null;

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                participant = new Participant(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("age")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return participant;
    }
}

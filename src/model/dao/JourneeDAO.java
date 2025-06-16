package model.dao;

import model.data.persistence.Journee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JourneeDAO extends DAO<Journee> {

    @Override
    public List<Journee> findAll() {
        List<Journee> journees = new ArrayList<>();
        String sql = "SELECT jour, mois, annee FROM journee";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                journees.add(new Journee(
                    rs.getInt("jour"),
                    rs.getInt("mois"),
                    rs.getInt("annee")
                ));
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return journees;
    }

    public Journee findByDate(int jour, int mois, int annee) {
        String sql = "SELECT jour, mois, annee FROM journee WHERE jour = ? AND mois = ? AND annee = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, jour);
            pstmt.setInt(2, mois);
            pstmt.setInt(3, annee);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Journee(jour, mois, annee);
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int update(Journee element) {
        // Journée est immuable dans cette implémentation
        return 0;
    }

    @Override
    public int delete(Journee element) {
        String sql = "DELETE FROM journee WHERE jour = ? AND mois = ? AND annee = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, element.getJour());
            pstmt.setInt(2, element.getMois());
            pstmt.setInt(3, element.getAnnee());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int create(Journee element) {
        String sql = "INSERT INTO journee (jour, mois, annee) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, element.getJour());
            pstmt.setInt(2, element.getMois());
            pstmt.setInt(3, element.getAnnee());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
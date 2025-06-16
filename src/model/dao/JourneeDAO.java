package model.dao;

import model.data.persistence.Journee;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JourneeDAO extends DAO<Journee> {

    @Override
    public List<Journee> findAll() {
        List<Journee> journees = new ArrayList<>();
        String sql = "SELECT jour FROM journee"; // Champ DATE MySQL
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                LocalDate localDate = rs.getDate("jour").toLocalDate();
                journees.add(new Journee(
                    localDate.getDayOfMonth(),
                    localDate.getMonthValue(),
                    localDate.getYear()
                ));
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return journees;
    }

    public Journee findSecouristeByDate(int jour, int mois, int annee) {
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
    public int create(Journee element) {
        String sql = "INSERT INTO journee () VALUES (?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Convertir les entiers en java.sql.Date
            LocalDate date = LocalDate.of(
                element.getAnnee(),
                element.getMois(),
                element.getJour()
            );
            pstmt.setDate(1, java.sql.Date.valueOf(date));
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(Journee element) {
        // Journée est immuable dans cette implémentation
        return 0;
    }

    @Override
    public int delete(Journee element) {
        String sql = "DELETE FROM journee WHERE jour = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            LocalDate date = LocalDate.of(
                element.getAnnee(),
                element.getMois(),
                element.getJour()
            );
            pstmt.setDate(1, java.sql.Date.valueOf(date));
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
}
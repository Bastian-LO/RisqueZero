package model.dao;

import model.data.persistence.Competence;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompetenceDAO extends DAO<Competence> {

    @Override
    public int create(Competence competence) {
        String sql = "INSERT INTO competence (intitule) VALUES (?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, competence.getIntitule());
            return pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Competence findByIntitule(String intitule) {
        String sql = "SELECT intitule FROM competence WHERE intitule = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, intitule);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Competence(rs.getString("intitule"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int update(Competence competence) {
        String sql = "UPDATE competence SET intitule = ? WHERE intitule = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, competence.getIntitule());
            pstmt.setString(2, competence.getIntitule());
            return pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(Competence competence) {
        String sql = "DELETE FROM competence WHERE intitule = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, competence.getIntitule());
            return pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Competence> findAll() {
        List<Competence> competences = new ArrayList<>();
        String sql = "SELECT intitule FROM competence";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                competences.add(new Competence(rs.getString("intitule")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return competences;
    }
}
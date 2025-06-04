// SecouristeDAO.java
package model.dao;

import model.data.persistence.Secouriste;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SecouristeDAO extends DAO<Secouriste> {

    @Override
    public List<Secouriste> findAll() {
        List<Secouriste> secouristes = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, date_naissance, email, tel, adresse FROM secouriste";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                secouristes.add(new Secouriste(
                    rs.getLong("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("date_naissance"),
                    rs.getString("email"),
                    rs.getString("tel"),
                    rs.getString("adresse"),
                    new HashSet<>() // Disponibilités chargées séparément
                ));
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return secouristes;
    }

    public Secouriste findById(long id) {
        String sql = "SELECT nom, prenom, date_naissance, email, tel, adresse FROM secouriste WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Secouriste(
                        id,
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("date_naissance"),
                        rs.getString("email"),
                        rs.getString("tel"),
                        rs.getString("adresse"),
                        new HashSet<>() // Disponibilités chargées séparément
                    );
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int update(Secouriste element) {
        String sql = "UPDATE secouriste SET nom = ?, prenom = ?, date_naissance = ?, "
                   + "email = ?, tel = ?, adresse = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, element.getNom());
            pstmt.setString(2, element.getPrenom());
            pstmt.setString(3, element.getDateNaissance());
            pstmt.setString(4, element.getEmail());
            pstmt.setString(5, element.getTel());
            pstmt.setString(6, element.getAdresse());
            pstmt.setLong(7, element.getId());
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(Secouriste element) {
        String sql = "DELETE FROM secouriste WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, element.getId());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int create(Secouriste element) {
        String sql = "INSERT INTO secouriste (id, nom, prenom, date_naissance, email, tel, adresse) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, element.getId());
            pstmt.setString(2, element.getNom());
            pstmt.setString(3, element.getPrenom());
            pstmt.setString(4, element.getDateNaissance());
            pstmt.setString(5, element.getEmail());
            pstmt.setString(6, element.getTel());
            pstmt.setString(7, element.getAdresse());
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
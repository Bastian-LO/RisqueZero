package model.dao;

import model.data.persistence.Sport;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Sport
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class SportDAO extends DAO<Sport> {

    /**
     * Returns all the sports in the database
     * @return a list of all the sports
     */
    @Override
    public List<Sport> findAll() {
        List<Sport> sports = new ArrayList<>();
        String sql = "SELECT code, nom FROM sport";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                sports.add(new Sport(
                    rs.getString("code"),
                    rs.getString("nom")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sports;
    }

    /**
     * Returns the sport with the given code
     * @param code the code of the sport
     * @return the sport
     */
    public Sport findByCode(String code) {
        String sql = "SELECT nom FROM sport WHERE code = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Sport(code, rs.getString("nom"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the sport with the given code
     * @param element the sport
     * @return the number of rows affected
     */
    @Override
    public int update(Sport element) {
        String sql = "UPDATE sport SET nom = ? WHERE code = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, element.getNom());
            pstmt.setString(2, element.getCode());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Deletes the sport with the given code
     * @param element the sport
     * @return the number of rows affected
     */
    @Override
    public int delete(Sport element) {
        String sql = "DELETE FROM sport WHERE code = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, element.getCode());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Creates a new sport in the database
     * @param element the sport
     * @return the number of rows affected
     */
    @Override
    public int create(Sport element) {
        String sql = "INSERT INTO sport (code, nom) VALUES (?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, element.getCode());
            pstmt.setString(2, element.getNom());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
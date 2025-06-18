package model.dao;

import model.data.persistence.Site;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Site
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class SiteDAO extends DAO<Site> {

    //============================
    //          METHODS
    //=============================

    
    /**
     * Retrieves all sites from the database.
     *
     * @return a list of all sites, each represented as a Site object, or an empty list if no sites are found.
     * In case of a database access error, the error is printed and an empty list is returned.
     */
    @Override
    public List<Site> findAll() {
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT code, nom, longitude, latitude FROM site";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                sites.add(new Site(
                    rs.getString("code"),
                    rs.getString("nom"),
                    rs.getFloat("longitude"),
                    rs.getFloat("latitude")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sites;
    }

    /**
     * Retrieves a Site object from the database based on the given code.
     *
     * @param code the unique code of the site to retrieve
     * @return a Site object if found, otherwise null
     * In case of a database access error, the error is printed, and null is returned.
     */
    public Site findByCode(String code) {
        String sql = "SELECT nom, longitude, latitude FROM site WHERE code = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Site(
                        code,
                        rs.getString("nom"),
                        rs.getFloat("longitude"),
                        rs.getFloat("latitude")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Updates the site with the given code in the database with the new values given in the Site object.
     *
     * @param element the Site object with the new values
     * @return the number of rows affected (1 if the update was successful, 0 if no row was found with the given code)
     * In case of a database access error, the error is printed, and 0 is returned.
     */
    @Override
    public int update(Site element) {
        String sql = "UPDATE site SET nom = ?, longitude = ?, latitude = ? WHERE code = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, element.getNom());
            pstmt.setFloat(2, element.getLongitude());
            pstmt.setFloat(3, element.getLatitude());
            pstmt.setString(4, element.getCode());
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Deletes the site with the given code from the database.
     *
     * @param element the Site object with the code of the site to delete
     * @return the number of rows affected (1 if the delete was successful, 0 if no row was found with the given code)
     * In case of a database access error, the error is printed, and 0 is returned.
     */
    @Override
    public int delete(Site element) {
        String sql = "DELETE FROM site WHERE code = ?";
        
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
     * Creates a new site in the database.
     *
     * @param element the Site object to create
     * @return the number of rows affected (1 if the creation was successful, 0 if the site already exists)
     * In case of a database access error, the error is printed, and 0 is returned.
     */
    @Override
    public int create(Site element) {
        String sql = "INSERT INTO site (code, nom, longitude, latitude) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, element.getCode());
            pstmt.setString(2, element.getNom());
            pstmt.setFloat(3, element.getLongitude());
            pstmt.setFloat(4, element.getLatitude());
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
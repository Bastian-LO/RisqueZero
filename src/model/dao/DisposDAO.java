package model.dao;

import model.data.persistence.Dispos;
import model.data.persistence.Journee;
import model.data.persistence.Secouriste;
import model.data.service.DAOMngt;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * DAO for Dispos
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class DisposDAO extends DAO<Dispos> {

    //=================================
    //           ATTRIBUTES
    // ================================

    

    //=================================
    //           METHODS
    // ================================

    /**
     * Finds all disposibilities in the database
     * @return A list of all disponibilities, or an empty list if no disponibilities are found
     */
    @Override
    public List<Dispos> findAll() {
        List<Dispos> allDispos = new ArrayList<>();
        String sql = "SELECT id_sec, date_dispo, heure_debut, heure_fin FROM dispos";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Secouriste secouriste = DAOMngt.getSecouristeDAO().findById(rs.getLong("id_sec"));
                LocalDate localDate = rs.getDate("date_dispo").toLocalDate();
                Journee journee = new Journee(localDate);
                if (secouriste != null) {
                    allDispos.add(new Dispos(
                        secouriste,
                        journee,
                        parseTime(rs.getString("heure_debut")),
                        parseTime(rs.getString("heure_fin"))
                        
                    ));
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return allDispos;
    }

    /**
     * Finds all disponibilities for a given secouriste
     * @param secouristeId The ID of the secouriste
     * @return A set of all disponibilities for the given secouriste or an empty set if no disponibilities are found
     */
    public HashSet<Dispos> findBySecouriste(long secouristeId) {
        HashSet<Dispos> dispos = new HashSet<>();
        String sql = "SELECT date_dispo, heure_debut, heure_fin FROM dispos WHERE id_sec = ?";
        
        Secouriste secouriste = DAOMngt.getSecouristeDAO().findById(secouristeId);
        if (secouriste == null){
            return dispos;
        }
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, secouristeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate date = rs.getDate("date_dispo").toLocalDate();
                    dispos.add(new Dispos(
                        secouriste,
                        new Journee(date),
                        parseTime(rs.getString("heure_debut")),
                        parseTime(rs.getString("heure_fin"))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dispos;
    }

    /**
     * Creates a new disponibility in the database
     * @param dispos The disponibility to create
     * @return The number of rows affected by the query or 0 if an error occurs
     */
    @Override
    public int create(Dispos dispos) {
        if (dispos.getSecouriste() == null) return 0;
        
        String sql = "INSERT INTO dispos (id_sec, date_dispo, heure_debut, heure_fin) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, dispos.getSecouriste().getId());
            pstmt.setDate(2, java.sql.Date.valueOf(dispos.getDate().toLocalDate()));
            pstmt.setString(3, formatTime(dispos.getHeureDebut()));
            pstmt.setString(4, formatTime(dispos.getHeureFin()));
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Updates a disponibility in the database
     * @param dispos The disponibility to update
     * @return The number of rows affected by the query
     */
    @Override
    public int update(Dispos dispos) {
        String sql = "UPDATE dispos SET heure_debut = ?, heure_fin = ?, date_dispo = ? " +
                    "WHERE id_sec = ? AND date_dispo = ? AND heure_debut = ? AND heure_fin = ?";
        
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, formatTime(dispos.getHeureDebut()));
            pstmt.setString(2, formatTime(dispos.getHeureFin()));
            pstmt.setDate(3, java.sql.Date.valueOf(dispos.getDate().toLocalDate()));
            
            pstmt.setLong(4, dispos.getSecouriste().getId());
            pstmt.setDate(5, java.sql.Date.valueOf(dispos.getDate().toLocalDate()));
            pstmt.setString(6, formatTime(dispos.getHeureDebut()));
            pstmt.setString(7, formatTime(dispos.getHeureFin()));    
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Deletes a disponibility from the database
     * @param dispos The disponibility to delete
     * @return The number of rows affected by the query or 0 if an error occurs
     */
    @Override
    public int delete(Dispos dispos) {
        String sql = "DELETE FROM dispos WHERE id_sec = ? AND date_dispo = ? AND heure_debut = ? AND heure_fin = ?";
        
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, dispos.getSecouriste().getId());
            pstmt.setDate(2, java.sql.Date.valueOf(dispos.getDate().toLocalDate()));
            pstmt.setString(3, formatTime(dispos.getHeureDebut()));
            pstmt.setString(4, formatTime(dispos.getHeureFin()));
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
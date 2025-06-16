package model.dao;

import model.data.persistence.Dispos;
import model.data.persistence.Journee;
import model.data.persistence.Secouriste;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * DAO for Dispos
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class DisposDAO extends DAO<Dispos> {

    //=================================
    //           ATTRIBUTES
    // ================================

    /** DAO for Secouriste */
    private final SecouristeDAO secouristeDAO = new SecouristeDAO();

    //=================================
    //           METHODS
    // ================================

    /**
     * Finds all disposibilities in the database
     * @return A list of all disponibilities
     */
    @Override
    public List<Dispos> findAll() {
        List<Dispos> allDispos = new ArrayList<>();
        String sql = "SELECT id_sec, date_dispo, heure_debut, heure_fin FROM dispos";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Secouriste secouriste = secouristeDAO.findById(rs.getLong("id_sec"));
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
     * @return A set of all disponibilities for the given secouriste
     */
    public Set<Dispos> findBySecouriste(long secouristeId) {
        Set<Dispos> dispos = new HashSet<>();
        String sql = "SELECT date_dispo, heure_debut, heure_fin FROM dispos WHERE id_sec = ?";
        
        Secouriste secouriste = secouristeDAO.findById(secouristeId);
        if (secouriste == null) return dispos;
        
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

    /**
     * Creates a new disponibility in the database
     * @param dispos The disponibility to create
     * @return The number of rows affected by the query
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
            
            // Nouvelles valeurs
            pstmt.setString(1, formatTime(dispos.getHeureDebut()));
            pstmt.setString(2, formatTime(dispos.getHeureFin()));
            pstmt.setDate(3, java.sql.Date.valueOf(dispos.getDate().toLocalDate()));
            
            // Anciennes valeurs pour identification
            pstmt.setLong(4, dispos.getSecouriste().getId());
            pstmt.setDate(5, java.sql.Date.valueOf(dispos.getDate().toLocalDate()));
            pstmt.setString(6, formatTime(dispos.getHeureDebut())); // Ancienne heure début
            pstmt.setString(7, formatTime(dispos.getHeureFin()));    // Ancienne heure fin
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Parses a time string into an array of two integers
     * @param time String of the form "HH:MM"
     * @return An array of two integers: {hour, minute}
     */
    private int[] parseTime(String time) {
        String[] parts = time.split(":");
        return new int[]{
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1])
        };
    }

    /**
     * Formats an array of two integers into a string of the form "HH:MM"
     * @param time An array of two integers: {hour, minute}
     * @return A string of the form "HH:MM"
     */
    private String formatTime(int[] time) {
        return String.format("%02d:%02d", time[0], time[1]);
    }

}
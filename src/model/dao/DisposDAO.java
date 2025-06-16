package model.dao;

import model.data.persistence.Dispos;
import model.data.persistence.Journee;
import model.data.persistence.Secouriste;

import java.sql.*;
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
        String sql = "SELECT id_sec, jour, mois, annee, heure_debut, heure_fin FROM dispos";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Secouriste secouriste = secouristeDAO.findById(rs.getLong("id_sec"));
                if (secouriste != null) {
                    allDispos.add(new Dispos(
                        secouriste,
                        new Journee(
                            rs.getInt("jour"),
                            rs.getInt("mois"),
                            rs.getInt("annee")
                        ),
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
        String sql = "SELECT jour, mois, annee, heure_debut, heure_fin FROM dispos WHERE id_sec = ?";
        
        Secouriste secouriste = secouristeDAO.findById(secouristeId);
        if (secouriste == null) return dispos;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, secouristeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    dispos.add(new Dispos(
                        secouriste,
                        new Journee(
                            rs.getInt("jour"),
                            rs.getInt("mois"),
                            rs.getInt("annee")
                        ),
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
     * Deletes a disponibility from the database
     * @param dispos The disponibility to delete
     * @return The number of rows affected by the query
     */
    @Override
    public int delete(Dispos dispos) {
        String sql = "DELETE FROM dispos WHERE id_sec = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, dispos.getSecouriste().getId());
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
        
        String sql = "INSERT INTO dispos (id_sec, jour, mois, annee, heure_debut, heure_fin) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, dispos.getSecouriste().getId());
            pstmt.setInt(2, dispos.getDate().getJour());
            pstmt.setInt(3, dispos.getDate().getMois());
            pstmt.setInt(4, dispos.getDate().getAnnee());
            pstmt.setString(5, formatTime(dispos.getHeureDebut()));
            pstmt.setString(6, formatTime(dispos.getHeureFin()));
            
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
        String sql = "UPDATE dispos SET heure_debut = ?, heure_fin = ? "
                   + "WHERE id_sec = ? AND jour = ? AND mois = ? AND annee = ?";
        
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            long secouristeId = dispos.getSecouriste().getId();
            pstmt.setString(1, formatTime(dispos.getHeureDebut()));
            pstmt.setString(2, formatTime(dispos.getHeureFin()));
            pstmt.setLong(3, secouristeId);
            pstmt.setInt(4, dispos.getDate().getJour());
            pstmt.setInt(5, dispos.getDate().getMois());
            pstmt.setInt(6, dispos.getDate().getAnnee());
            
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
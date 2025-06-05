package model.dao;

import model.data.persistence.Dispos;
import model.data.persistence.Journee;
import model.data.persistence.Secouriste;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DisposDAO extends DAO<Dispos> {

    private final SecouristeDAO secouristeDAO = new SecouristeDAO();

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

    private int[] parseTime(String time) {
        String[] parts = time.split(":");
        return new int[]{
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1])
        };
    }

    private String formatTime(int[] time) {
        return String.format("%02d:%02d", time[0], time[1]);
    }

}
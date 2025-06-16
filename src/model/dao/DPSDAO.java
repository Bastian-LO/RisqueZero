package model.dao;

import model.data.persistence.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DPSDAO extends DAO<DPS> {

    private final CompetenceDAO competenceDAO = new CompetenceDAO();
    private final SiteDAO siteDAO = new SiteDAO();
    private final SportDAO sportDAO = new SportDAO();

     @Override
    public List<DPS> findAll() {
        List<DPS> dpsList = new ArrayList<>();
        String sql = "SELECT id FROM dps";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                DPS dps = findById(rs.getLong("id"));
                if (dps != null) {
                    dpsList.add(dps);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dpsList;
    }

    public DPS findById(long id) {
       String sql = "SELECT id, horaire_depart, horaire_fin, date_evt, id_site, id_sport, id_competence_principale FROM dps WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Construction du DPS
                int[] heureDepart = parseTime(rs.getString("horaire_depart"));
                int[] heureFin = parseTime(rs.getString("horaire_fin"));
                Journee journee = new Journee(
                    rs.getInt("jour"),
                    rs.getInt("mois"),
                    rs.getInt("annee")
                );
                
                Site site = siteDAO.findByCode(rs.getString("id_site"));
                Sport sport = sportDAO.findByCode(rs.getString("id_sport"));
                
                // Récupération de la compétence principale ?
                ArrayList<Competence> competences = new ArrayList<>();
                Competence comp = competenceDAO.findByIntitule(rs.getString("id_competence_principale"));
                if (comp != null) {
                    competences.add(comp);
                }
                
                return new DPS(
                    id,
                    heureDepart,
                    heureFin,
                    journee,
                    competences,
                    site,
                    sport
                );
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int create(DPS dps) {
        String sql = "INSERT INTO dps (id, horaire_depart, horaire_fin, jour, mois, annee, " +
                     "id_site, id_sport, id_competence_principale) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, dps.getId());
            pstmt.setString(2, formatTime(dps.getHoraireDepart()));
            pstmt.setString(3, formatTime(dps.getHoraireFin()));
            pstmt.setInt(4, dps.getDateEvt().getJour());
            pstmt.setInt(5, dps.getDateEvt().getMois());
            pstmt.setInt(6, dps.getDateEvt().getAnnee());
            pstmt.setString(7, dps.getLieu().getCode());
            pstmt.setString(8, dps.getSport().getCode());
            
            // Competence principale ?
            pstmt.setString(9, dps.getCompetences().get(0).getIntitule());
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(DPS dps) {
        String sql = "UPDATE dps SET horaire_depart = ?, horaire_fin = ?, " +
                     "jour = ?, mois = ?, annee = ?, id_site = ?, id_sport = ?, " +
                     "id_competence_principale = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, formatTime(dps.getHoraireDepart()));
            pstmt.setString(2, formatTime(dps.getHoraireFin()));
            pstmt.setInt(3, dps.getDateEvt().getJour());
            pstmt.setInt(4, dps.getDateEvt().getMois());
            pstmt.setInt(5, dps.getDateEvt().getAnnee());
            pstmt.setString(6, dps.getLieu().getCode());
            pstmt.setString(7, dps.getSport().getCode());
            pstmt.setString(8, dps.getCompetences().get(0).getIntitule());
            pstmt.setLong(9, dps.getId());
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(DPS dps) {
        String sql = "DELETE FROM dps WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, dps.getId());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int[] parseTime(String time) {
        String[] parts = time.split(":");
        return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }

    private String formatTime(int[] time) {
        return String.format("%02d:%02d", time[0], time[1]);
    }
}
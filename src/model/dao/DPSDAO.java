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
        String sql = "SELECT d.id, d.horaire_depart, d.horaire_fin, d.date_event, "
                   + "d.id_site, d.id_sport FROM dps d WHERE d.id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Construction de base
                int[] heureDepart = parseTime(rs.getString("horaire_depart"));
                int[] heureFin = parseTime(rs.getString("horaire_fin"));
                Journee journee = new Journee(rs.getDate("date_event").toLocalDate());
                Site site = siteDAO.findByCode(rs.getString("id_site"));
                Sport sport = sportDAO.findByCode(rs.getString("id_sport"));
                
                // Récupération des compétences
                ArrayList<Competence> competences = new ArrayList<>();
                String compSql = "SELECT competence FROM dps_competence WHERE id_dps = ?";
                try (PreparedStatement compPstmt = conn.prepareStatement(compSql)) {
                    compPstmt.setLong(1, id);
                    ResultSet compRs = compPstmt.executeQuery();
                    while (compRs.next()) {
                        Competence comp = competenceDAO.findByIntitule(compRs.getString("competence"));
                        if (comp != null) competences.add(comp);
                    }
                }
                
                return new DPS(id, heureDepart, heureFin, journee, competences, site, sport);
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Competence> findCompetencesByDPSId(long idDPS) {
        List<Competence> competences = new ArrayList<>();
        String sql = "SELECT competence FROM dps_competence WHERE id_dps = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, idDPS);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Competence comp = competenceDAO.findByIntitule(rs.getString("competence"));
                if (comp != null) {
                    competences.add(comp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return competences;
    }

    @Override
    public int create(DPS dps) {
        String sql = "INSERT INTO dps (id, horaire_depart, horaire_fin, date_event, id_site, id_sport) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, dps.getId());
            pstmt.setString(2, formatTime(dps.getHoraireDepart()));
            pstmt.setString(3, formatTime(dps.getHoraireFin()));
            pstmt.setDate(4, java.sql.Date.valueOf(dps.getDateEvt().toLocalDate()));
            pstmt.setString(5, dps.getLieu().getCode());
            pstmt.setString(6, dps.getSport().getCode());
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(DPS dps) {
        String sql = "UPDATE dps SET horaire_depart = ?, horaire_fin = ?, date_event = ?, id_site = ?, id_sport = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, formatTime(dps.getHoraireDepart()));
            pstmt.setString(2, formatTime(dps.getHoraireFin()));
            pstmt.setDate(3, java.sql.Date.valueOf(dps.getDateEvt().toLocalDate()));
            pstmt.setString(6, dps.getLieu().getCode());
            pstmt.setString(7, dps.getSport().getCode());
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
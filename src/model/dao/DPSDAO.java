package model.dao;

import model.data.persistence.*;
import model.data.service.DAOMngt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for DPS
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class DPSDAO extends DAO<DPS> {

    //===============================
    //            METHODS
    //===============================

    /**
     * Retrieves all DPS records from the database.
     *
     * @return a list of all DPS objects, or an empty list if no records are found.
     * In case of a database access error, the error is printed and the method returns an empty list.
     */
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

    /**
     * Retrieves a DPS object from the database based on its id.
     * @param id the id of the DPS
     * @return the DPS object if found, otherwise nothing
     * @throws IllegalArgumentException if the id is invalid
     */
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
                Site site = DAOMngt.getSiteDAO().findByCode(rs.getString("id_site"));
                Sport sport = DAOMngt.getSportDAO().findByCode(rs.getString("id_sport"));
                
                // Récupération des compétences
                ArrayList<Competence> competences = new ArrayList<>();
                String compSql = "SELECT competence FROM dps_competence WHERE id_dps = ?";
                try (PreparedStatement compPstmt = conn.prepareStatement(compSql)) {
                    compPstmt.setLong(1, id);
                    ResultSet compRs = compPstmt.executeQuery();
                    while (compRs.next()) {
                        Competence comp = DAOMngt.getCompetenceDAO().findByIntitule(compRs.getString("competence"));
                        if (comp != null) competences.add(comp);
                    }
                }
                
                return new DPS(id, heureDepart, heureFin, journee, competences, site, sport);
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        
        throw new IllegalArgumentException("DPS with id " + id + " not found");
    }

    /**
     * Retrieves a list of competences associated with a given DPS id.
     * @param idDPS the id of the DPS
     * @return a list of competences, or an empty list if no competences are found
     */
    private List<Competence> findCompetencesByDPSId(long idDPS) {
        List<Competence> competences = new ArrayList<>();
        String sql = "SELECT competence FROM dps_competence WHERE id_dps = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, idDPS);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Competence comp = DAOMngt.getCompetenceDAO().findByIntitule(rs.getString("competence"));
                if (comp != null) {
                    competences.add(comp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return competences;
    }
    
    /**
     * Creates a new DPS record in the database.
     * @param dps the DPS object to be created
     * @return the number of rows affected, or 0 if an error occurs
     */
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

    /**
     * Updates a DPS record in the database.
     * @param dps the DPS object to be updated
     * @return the number of rows affected, or 0 if an error occurs
     */
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

    /**
     * Deletes a DPS record from the database based on its id.
     * @param dps the DPS object containing the id of the record to be deleted
     * @return the number of rows affected by the query, or 0 if an error occurs
    */
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

}
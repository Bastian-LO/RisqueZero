package model.dao;

import model.data.persistence.Affectation;
import model.data.persistence.Competence;
import model.data.persistence.DPS;
import model.data.persistence.Secouriste;
import model.data.service.DAOMngt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

/**
 * DAO for Affectation
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class AffectationDAO extends DAO<Affectation> {    

    /**
     * Retrieves all Affectation records from the database.
     * 
     * @return a list of all Affectation objects, or an empty list if no records are found.
     * In case of a database access error, the error is printed and the method returns an empty list.
     */
    @Override
    public List<Affectation> findAll() {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT a.id_sec, a.id_dps, a.competence FROM affectation a";
        
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                long dpsId = rs.getLong("id_dps");
                DPS dps = DAOMngt.getDPSDAO().findById(dpsId);
                
                if (dps != null) {
                        
                    Affectation existing = null;
                    for (Affectation aff : affectations) {
                        if (aff.getDps().getId() == dpsId) {
                            existing = aff;
                            break;
                        }
                    }
                    
                    if (existing == null) {
                        existing = new Affectation(new ArrayList<>(), dps);
                        affectations.add(existing);
                    }
                    
                    // Ajouter la paire secouriste/compétence
                    Secouriste sec = DAOMngt.getSecouristeDAO().findById(rs.getLong("id_sec"));
                    Competence comp = new Competence(rs.getString("competence"));
                    existing.getList().add(new Pair<>(sec, comp));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectations;
    }

    /**
     * Creates a new affectation in the database
     * @return the number of affectations created, or 0 if an error occurs
     */
    @Override
    public int create(Affectation affectation) {
        String sql = "INSERT INTO affectation (id_sec, id_dps, competence) VALUES (?, ?, ?)";
        int count = 0;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Pair<Secouriste, Competence> pair : affectation.getList()) {
                pstmt.setLong(1, pair.getKey().getId());
                pstmt.setLong(2, affectation.getDps().getId());
                pstmt.setString(3, pair.getValue().getIntitule());
                pstmt.addBatch();
                count++;
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return count;
    }

    /**
     * Updates an affectation
     * @return the number of affectations updated or 0 if an error occurs
     */
    @Override
    public int update(Affectation affectation) {
        // D'abord supprimer les anciennes entrées
        delete(affectation);
        // Puis recréer les nouvelles
        return create(affectation);
    }

    /**
     * Deletes an affectation 
     * @return the number of affectations deleted or 0 if an error occurs
     */
    @Override
    public int delete(Affectation affectation) {
        String sql = "DELETE FROM affectation WHERE id_sec = ? AND id_dps = ? AND competence = ?";
        int count = 0;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Pair<Secouriste, Competence> pair : affectation.getList()) {
                pstmt.setLong(1, pair.getKey().getId());
                pstmt.setLong(2, affectation.getDps().getId());
                pstmt.setString(3, pair.getValue().getIntitule());
                pstmt.addBatch();
                count++;
            }   
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return count;
    }

    /**
     * Retrieves all affectations for a given secouriste
     * @param idSecouriste the id of the secouriste
     * @return a list of affectations, or an empty list if no affectations are found
     */
    public List<Affectation> findBySecouriste(long idSecouriste) {
        List<Affectation> result = new ArrayList<>();
        String sql = "SELECT DISTINCT id_dps FROM affectation WHERE id_sec = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, idSecouriste);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    long dpsId = rs.getLong("id_dps");
                    DPS dps = DAOMngt.getDPSDAO().findById(dpsId);
                    if (dps != null) {
                        // Récupérer toutes les compétences pour ce secouriste et ce DPS
                        String compSql = "SELECT competence FROM affectation WHERE id_sec = ? AND id_dps = ?";
                        try (PreparedStatement compPstmt = conn.prepareStatement(compSql)) {
                            compPstmt.setLong(1, idSecouriste);
                            compPstmt.setLong(2, dpsId);
                            ResultSet compRs = compPstmt.executeQuery();
                            
                            ArrayList<Pair<Secouriste, Competence>> pairs = new ArrayList<>();
                            Secouriste sec = DAOMngt.getSecouristeDAO().findById(idSecouriste);
                            
                            while (compRs.next()) {
                                Competence comp = DAOMngt.getCompetenceDAO().findByIntitule(compRs.getString("competence"));
                                if (comp != null) {
                                    pairs.add(new Pair<>(sec, comp));
                                }
                            }
                            
                            if (!pairs.isEmpty()) {
                                result.add(new Affectation(pairs, dps));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Retrieves all affectations for a given DPS
     * @param idDPS the id of the DPS
     * @return a list of affectations, or an empty list if no affectations are found
     */
    public List<Affectation> findByDPS(long idDPS) {
        List<Affectation> result = new ArrayList<>();
        String sql = "SELECT DISTINCT id_sec FROM affectation WHERE id_dps = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, idDPS);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    long secId = rs.getLong("id_sec");
                    Secouriste sec = DAOMngt.getSecouristeDAO().findById(secId);
                    if (sec != null) {
                        // Récupérer toutes les compétences pour ce secouriste et ce DPS
                        String compSql = "SELECT competence FROM affectation WHERE id_sec = ? AND id_dps = ?";
                        try (PreparedStatement compPstmt = conn.prepareStatement(compSql)) {
                            compPstmt.setLong(1, secId);
                            compPstmt.setLong(2, idDPS);
                            ResultSet compRs = compPstmt.executeQuery();
                            
                            ArrayList<Pair<Secouriste, Competence>> pairs = new ArrayList<>();
                            DPS dps = DAOMngt.getDPSDAO().findById(idDPS);
                            
                            while (compRs.next()) {
                                Competence comp = DAOMngt.getCompetenceDAO().findByIntitule(compRs.getString("competence"));
                                if (comp != null) {
                                    pairs.add(new Pair<>(sec, comp));
                                }
                            }
                            
                            if (!pairs.isEmpty()) {
                                result.add(new Affectation(pairs, dps));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
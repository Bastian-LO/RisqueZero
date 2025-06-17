package model.dao;

import model.data.persistence.Affectation;
import model.data.persistence.Competence;
import model.data.persistence.DPS;
import model.data.persistence.Secouriste;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.util.Pair;

public class AffectationDAO extends DAO<Affectation> {

    private final SecouristeDAO secouristeDAO = new SecouristeDAO();
    private final DPSDAO dpsDAO = new DPSDAO();
    private final CompetenceDAO competenceDAO = new CompetenceDAO();

    @Override
    public List<Affectation> findAll() {
        Map<Long, Affectation> affectationMap = new HashMap<>();
        String sql = "SELECT a.id_sec, a.id_dps, a.competence FROM affectation a";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                long dpsId = rs.getLong("id_dps");
                Affectation affectation = affectationMap.get(dpsId);
                
                if (affectation == null) {
                    DPS dps = dpsDAO.findById(dpsId);
                    if (dps == null) continue;
                    
                    affectation = new Affectation(new ArrayList<>(), dps);
                    affectationMap.put(dpsId, affectation);
                }
                
                Secouriste sec = secouristeDAO.findById(rs.getLong("id_sec"));
                Competence comp = new Competence(rs.getString("competence"));
                affectation.getList().add(new Pair<>(sec, comp));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(affectationMap.values());
    }

    /**
     * Create a new affectation
     */
    @Override
    public int create(Affectation affectation) {
        String sql = "INSERT INTO affectation (id_sec, id_dps, competence) VALUES (?, ?, ?)";
        int count = 0;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Pair<Secouriste, Competence> pair : affectation.getList()) {
                pstmt.setLong(1, pair.getKey().getId());
                pstmt.setLong(2, affectation.getIdDps().getId());
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
     */
    @Override
    public int update(Affectation affectation) {
        // D'abord supprimer les anciennes entrées
        delete(affectation);
        // Puis recréer les nouvelles
        return create(affectation);
    }

    /**
     * Delete an affectation
     */
    @Override
    public int delete(Affectation affectation) {
        String sql = "DELETE FROM affectation WHERE id_sec = ? AND id_dps = ? AND competence = ?";
        int count = 0;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Pair<Secouriste, Competence> pair : affectation.getList()) {
                pstmt.setLong(1, pair.getKey().getId());
                pstmt.setLong(2, affectation.getIdDps().getId());
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
     * 
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
                    DPS dps = dpsDAO.findById(dpsId);
                    if (dps != null) {
                        // Récupérer toutes les compétences pour ce secouriste et ce DPS
                        String compSql = "SELECT competence FROM affectation WHERE id_sec = ? AND id_dps = ?";
                        try (PreparedStatement compPstmt = conn.prepareStatement(compSql)) {
                            compPstmt.setLong(1, idSecouriste);
                            compPstmt.setLong(2, dpsId);
                            ResultSet compRs = compPstmt.executeQuery();
                            
                            ArrayList<Pair<Secouriste, Competence>> pairs = new ArrayList<>();
                            Secouriste sec = secouristeDAO.findById(idSecouriste);
                            
                            while (compRs.next()) {
                                Competence comp = competenceDAO.findByIntitule(compRs.getString("competence"));
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
    
    public List<Affectation> findByDPS(long idDPS) {
        List<Affectation> result = new ArrayList<>();
        String sql = "SELECT DISTINCT id_sec FROM affectation WHERE id_dps = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, idDPS);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    long secId = rs.getLong("id_sec");
                    Secouriste sec = secouristeDAO.findById(secId);
                    if (sec != null) {
                        // Récupérer toutes les compétences pour ce secouriste et ce DPS
                        String compSql = "SELECT competence FROM affectation WHERE id_sec = ? AND id_dps = ?";
                        try (PreparedStatement compPstmt = conn.prepareStatement(compSql)) {
                            compPstmt.setLong(1, secId);
                            compPstmt.setLong(2, idDPS);
                            ResultSet compRs = compPstmt.executeQuery();
                            
                            ArrayList<Pair<Secouriste, Competence>> pairs = new ArrayList<>();
                            DPS dps = dpsDAO.findById(idDPS);
                            
                            while (compRs.next()) {
                                Competence comp = competenceDAO.findByIntitule(compRs.getString("competence"));
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
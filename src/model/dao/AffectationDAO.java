package model.dao;

import model.data.persistence.Affectation;
import model.data.persistence.Competence;
import model.data.persistence.DPS;
import model.data.persistence.Secouriste;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AffectationDAO extends DAO<Affectation> {

    private final SecouristeDAO secouristeDAO = new SecouristeDAO();
    private final DPSDAO dpsDAO = new DPSDAO();

    @Override
    public List<Affectation> findAll() {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT id_sec, id_dps, competence FROM affectation";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                HashSet<Secouriste> secouristes = new HashSet<>();
                Secouriste sec = secouristeDAO.findById(rs.getLong("id_sec"));
                if (sec != null) secouristes.add(sec);
                
                HashSet<DPS> dpsSet = new HashSet<>();
                DPS dps = dpsDAO.findById(rs.getLong("id_dps"));
                if (dps != null) dpsSet.add(dps);
                
                affectations.add(new Affectation(
                    secouristes,
                    dpsSet,
                    new Competence(rs.getString("competence"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectations;
    }

    /**
     * Crée une nouvelle affectation
     */
    @Override
    public int create(Affectation affectation) {
        String sql = "INSERT INTO affectation (id_sec, id_dps, competence) VALUES (?, ?, ?)";
        int count = 0;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Secouriste sec : affectation.getIdSec()) {
                for (DPS dps : affectation.getIdDps()) {
                    pstmt.setLong(1, sec.getId());
                    pstmt.setLong(2, dps.getId());
                    pstmt.setString(3, affectation.getCompetence().getIntitule());
                    pstmt.addBatch();
                    count++;
                }
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return count;
    }

    /**
     * Met à jour une affectation existante
     */
    @Override
    public int update(Affectation affectation) {
        // D'abord supprimer les anciennes entrées
        delete(affectation);
        // Puis recréer les nouvelles
        return create(affectation);
    }

    /**
     * Supprime une affectation spécifique
     */
    @Override
    public int delete(Affectation affectation) {
        String sql = "DELETE FROM affectation WHERE id_sec = ? AND id_dps = ? AND competence = ?";
        int count = 0;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Secouriste sec : affectation.getIdSec()) {
                for (DPS dps : affectation.getIdDps()) {
                    pstmt.setLong(1, sec.getId());
                    pstmt.setLong(2, dps.getId());
                    pstmt.setString(3, affectation.getCompetence().getIntitule());
                    pstmt.addBatch();
                    count++;
                }
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return count;
    }

    /**
     * Trouve toutes les affectations d'un secouriste
     */
    public List<Affectation> findBySecouriste(long idSecouriste) {
        List<Affectation> result = new ArrayList<>();
        String sql = "SELECT id_dps, competence FROM affectation WHERE id_sec = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, idSecouriste);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HashSet<Secouriste> secouristes = new HashSet<>();
                    secouristes.add(secouristeDAO.findById(idSecouriste));
                    
                    HashSet<DPS> dpsSet = new HashSet<>();
                    dpsSet.add(dpsDAO.findById(rs.getLong("id_dps")));
                    
                    result.add(new Affectation(
                        secouristes,
                        dpsSet,
                        new Competence(rs.getString("competence"))
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Trouve toutes les affectations pour un DPS
     */
    public List<Affectation> findByDPS(long idDPS) {
        List<Affectation> result = new ArrayList<>();
        String sql = "SELECT id_sec, competence FROM affectation WHERE id_dps = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, idDPS);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HashSet<Secouriste> secouristes = new HashSet<>();
                    secouristes.add(secouristeDAO.findById(rs.getLong("id_sec")));
                    
                    HashSet<DPS> dpsSet = new HashSet<>();
                    dpsSet.add(dpsDAO.findById(idDPS));
                    
                    result.add(new Affectation(
                        secouristes,
                        dpsSet,
                        new Competence(rs.getString("competence"))
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
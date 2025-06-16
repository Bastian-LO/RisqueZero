package model.dao;

import model.data.persistence.Competence;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompetenceDAO extends DAO<Competence> {

    @Override
    public int create(Competence competence) {
        String sql = "INSERT INTO competence (intitule) VALUES (?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, competence.getIntitule());
            int result = pstmt.executeUpdate();
            
            // Ajouter les prérequis si la création a réussi
            if (result > 0) {
                addRequis(competence);
            }
            
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Competence findByIntitule(String intitule) {
        String sql = "SELECT intitule FROM competence WHERE intitule = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, intitule);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Competence competence = new Competence(rs.getString("intitule"));
                // Charger les prérequis
                HashSet<Competence> requis = findRequisByCompetence(intitule);
                competence.setRequis(requis);
                return competence;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int update(Competence competence) {
        // On ne change pas l'intitulé car final
        // On met seulement à jour les relations de prérequis
        return updateRequis(competence);
    }

    @Override
    public int delete(Competence competence) {
        String sql = "DELETE FROM competence WHERE intitule = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, competence.getIntitule());
            return pstmt.executeUpdate();
            // Les prérequis seront supprimés automatiquement grâce à ON DELETE CASCADE
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Competence> findAll() {
        List<Competence> competences = new ArrayList<>();
        String sql = "SELECT intitule FROM competence";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String intitule = rs.getString("intitule");
                Competence competence = new Competence(intitule);
                // Charger les prérequis
                HashSet<Competence> requis = findRequisByCompetence(intitule);
                competence.setRequis(requis);
                competences.add(competence);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return competences;
    }

    private HashSet<Competence> findRequisByCompetence(String intituleCompetence) {
        HashSet<Competence> requis = new HashSet<>();
        String sql = "SELECT competence_requise FROM competence_requis WHERE competence_principale = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, intituleCompetence);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Competence compRequis = findByIntitule(rs.getString("competence_requise"));
                if (compRequis != null) {
                    requis.add(compRequis);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requis;
    }

    private int addRequis(Competence competence) {
        String sql = "INSERT INTO competence_requis VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            int count = 0;
            for (Competence req : competence.getRequis()) {
                pstmt.setString(1, competence.getIntitule());
                pstmt.setString(2, req.getIntitule());
                pstmt.addBatch();
                count++;
            }
            pstmt.executeBatch();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateRequis(Competence competence) {
        // D'abord supprimer les anciens prérequis
        deleteRequis(competence.getIntitule());
        // Puis ajouter les nouveaux
        return addRequis(competence);
    }

    private int deleteRequis(String competence) {
        String sql = "DELETE FROM competence_requis WHERE competence_principale = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, competence);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Competence> findCompetencesRequiring(String intituleCompetence) {
        List<Competence> competences = new ArrayList<>();
        String sql = "SELECT competence_principale FROM competence_requis WHERE competence_requise = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, intituleCompetence);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Competence competence = findByIntitule(rs.getString("competence_principale"));
                if (competence != null) {
                    competences.add(competence);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return competences;
    }
}
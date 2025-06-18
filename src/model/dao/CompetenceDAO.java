package model.dao;

import model.data.persistence.Competence;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * DAO for Competence
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class CompetenceDAO extends DAO<Competence> {

    /**
     * Creates a new competence in the database
     * @param competence the competence object to be created
     * @return the number of rows affected, or 0 if an error occurs
     */
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

    /**
     * Finds a competence by its intitule
     * @param intitule the intitule of the competence
     * @return the competence object, or nothing if not found
     * @throws IllegalArgumentException if the competence is not found
     */
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
        throw new IllegalArgumentException("Compétence : intitule '" + intitule + "' inconnu.");
    }

    /**
     * Updates a competence in the database
     * @param competence the competence object to be updated
     * @return the number of rows affected, or 0 if an error occurs
     */
    @Override
    public int update(Competence competence) {
        // On ne change pas l'intitulé car final
        // On met seulement à jour les relations de prérequis
        return updateRequis(competence);
    }

    /**
     * Deletes a competence from the database
     * @param competence the competence object to be deleted
     * @return the number of rows affected, or 0 if an error occurs
     */
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

    /**
     * Lists all competences in the database
     * @return a list of competence objects
     */
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

    /**
     * Finds all competences that are required by a given competence
     * @param intituleCompetence the intitule of the competence
     * @return a set of competence objects
     */
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

    /**
     * Adds all competences that are required by a given competence
     * @param competence the competence object to be updated
     * @return the number of rows affected, or 0 if an error occurs
     */
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

    /**
     * Updates all competences that are required by a given competence
     * @param competence the competence object to be updated
     * @return the number of rows affected, or 0 if an error occurs
     */
    public int updateRequis(Competence competence) {
        // D'abord supprimer les anciens prérequis
        deleteRequis(competence.getIntitule());
        // Puis ajouter les nouveaux
        return addRequis(competence);
    }

    /**
     * Deletes all competences that are required by a given competence
     * @param competence the competence object to be deleted
     * @return the number of rows affected, or 0 if an error occurs
     */
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

    /**
     * Lists all competences that are required by a given competence
     * @param intituleCompetence the intitule of the competence
     * @return a list of competence objects
     */
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
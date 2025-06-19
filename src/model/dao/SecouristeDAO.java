package model.dao;

import model.data.persistence.Competence;
import model.data.persistence.Dispos;
import model.data.persistence.Secouriste;
import model.data.service.DAOMngt;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * DAO for Secouriste
 * @author Bastian LEOUEDEC, Killian AVRIL, Enrick MANANJEAN, Elwan YVIN, Emile THEVENIN
 */
public class SecouristeDAO extends DAO<Secouriste> {
    //================================
    //          ATTRIBUTES
    //================================

    //================================
    //          METHODS
    //================================

    /**
     * Retrieves all Secouriste records from the database.
     *
     * @return a list of all Secouriste objects, or an empty list if no records are found.
     * In case of a database access error, the error is printed and the method returns an empty list.
     */
    @Override
    public List<Secouriste> findAll() {
        List<Secouriste> secouristes = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, date_naissance, email, tel, adresse FROM secouriste";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                long id = rs.getLong("id");
                // Charger les compétences séparément
                ArrayList<Competence> competences = findCompetencesBySecouristeId(id);
                // Charger les disponibilités séparément
                HashSet<Dispos> disponibilites = new HashSet<>(DAOMngt.getDisposDAO().findBySecouriste(id));
                
                secouristes.add(new Secouriste(
                    id,
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("date_naissance"),
                    rs.getString("email"),
                    rs.getString("tel"),
                    rs.getString("adresse"),
                    competences,
                    disponibilites
                ));
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return secouristes;
    }

    /**
     * Retrieves a Secouriste object from the database based on its id.
     *
     * @param id the id of the Secouriste to retrieve
     * @return the Secouriste object if found, otherwise null
     * In case of a database access error, the error is printed and the method returns null.
     */
    public Secouriste findById(long id) {
        String sql = "SELECT nom, prenom, date_naissance, email, tel, adresse "
                   + "FROM secouriste WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Récupérer les compétences
                    ArrayList<Competence> competences = new ArrayList<>();
                    String compSql = "SELECT competence FROM secouriste_competence WHERE id_sec = ?";
                    try (PreparedStatement compStmt = conn.prepareStatement(compSql)) {
                        compStmt.setLong(1, id);
                        ResultSet compRs = compStmt.executeQuery();
                        while (compRs.next()) {
                            competences.add(new Competence(compRs.getString("competence")));
                        }
                    }
                    
                    return new Secouriste(
                        id,
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("date_naissance"),
                        rs.getString("email"),
                        rs.getString("tel"),
                        rs.getString("adresse"),
                        competences,
                        new HashSet<>()
                    );
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Secouriste with id " + id + " not found.");
    }

    /**
     * Retrieves a list of competences associated with a given Secouriste id.
     * @param idSecouriste the id of the Secouriste
     * @return a list of competences, or an empty list if no competences are found
     */
    private ArrayList<Competence> findCompetencesBySecouristeId(long idSecouriste) {
        ArrayList<Competence> competences = new ArrayList<>();
        String sql = "SELECT competence FROM secouriste_competence WHERE id_sec = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, idSecouriste);
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
  
    @Override
    public int update(Secouriste element) {
        String sql = "UPDATE secouriste SET nom = ?, prenom = ?, date_naissance = ?, "
                   + "email = ?, tel = ?, adresse = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, element.getNom());
            pstmt.setString(2, element.getPrenom());
            pstmt.setString(3, element.getDateNaissance());
            pstmt.setString(4, element.getEmail());
            pstmt.setString(5, element.getTel());
            pstmt.setString(6, element.getAdresse());
            pstmt.setLong(7, element.getId());
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(Secouriste element) {
        String sql = "DELETE FROM secouriste WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, element.getId());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int create(Secouriste element) {
        String sql = "INSERT INTO secouriste (id, nom, prenom, date_naissance, email, tel, adresse) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, element.getId());
            pstmt.setString(2, element.getNom());
            pstmt.setString(3, element.getPrenom());
            pstmt.setString(4, element.getDateNaissance());
            pstmt.setString(5, element.getEmail());
            pstmt.setString(6, element.getTel());
            pstmt.setString(7, element.getAdresse());
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}